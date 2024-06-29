package one.taya;

import java.util.Optional;

import org.luaj.vm2.LuaError;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.minecraft.util.Formatting;

public class LuaCommand {

	public static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> {
		LuaScriptManager luaScriptManager = LuaCraft.LUA_SCRIPT_MANAGER;
		CommandSource.suggestIdentifiers(luaScriptManager.getLuaScriptTags(), builder, "#");
		return CommandSource.suggestIdentifiers(luaScriptManager.getAllLuaScripts(), builder);
	};

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

    LiteralCommandNode<ServerCommandSource> luaCommand = CommandManager
    .literal("lua")
    .requires(source -> source.hasPermissionLevel(2))
    .then(CommandManager.literal("run")
      .then(CommandManager.argument("source", StringArgumentType.greedyString())
        .executes(LuaCommand::executeRun)))
    .then(CommandManager.argument("name", IdentifierArgumentType.identifier())
      .suggests(SUGGESTION_PROVIDER)
      .executes(LuaCommand::execute))
    .build();

    dispatcher.getRoot().addChild(luaCommand);
  }

  private static int execute(CommandContext<ServerCommandSource> context) {
    // Execute the Lua script

    Identifier identifier = IdentifierArgumentType.getIdentifier(context, "name");

    Optional<LuaScript> result = LuaCraft.LUA_SCRIPT_MANAGER.getLuaScript(identifier);

    if(!result.isPresent()) {
      context.getSource().sendFeedback(() -> Text.literal("Lua script " + identifier.toString() + " not found"), false);
      return 0;
    }

    LuaScript luascript = result.get();
    
    try {
      LuaCraft.LUA_SCRIPT_MANAGER.execute(luascript);
    } catch(LuaError e) {
      sendErrorMessage(context.getSource(), e);
      return 0;
    }


    return 1;
  }

  private static int executeRun(CommandContext<ServerCommandSource> context) {
    // Execute the Lua string
    String source = StringArgumentType.getString(context, "source");

    LuaScript luascript = new LuaScript(Identifier.of("luascript", "run"), source);

    try {
      LuaCraft.LUA_SCRIPT_MANAGER.execute(luascript);
    } catch(LuaError e) {
      sendErrorMessage(context.getSource(), e);
      return 0;
    }

    return 1;
  }

  private static void sendErrorMessage(ServerCommandSource source, LuaError e) {
    source.sendFeedback(() -> Text.literal(e.getMessage()).formatted(Formatting.RED), false);
  }

}
