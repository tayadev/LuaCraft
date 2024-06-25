package one.taya;

import java.util.Optional;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LuaCommand {
  
  // Implement the `/lua ns:folder/script` command

	public static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> {
		LuaScriptManager luaScriptManager = LuaCraft.LUA_SCRIPT_MANAGER;
		CommandSource.suggestIdentifiers(luaScriptManager.getLuaScriptTags(), builder, "#");
		return CommandSource.suggestIdentifiers(luaScriptManager.getAllLuaScripts(), builder);
	};

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(
      CommandManager.literal("lua")
        .requires(source -> source.hasPermissionLevel(2))
        .then(
          CommandManager.argument("name", new IdentifierArgumentType())
          .suggests(SUGGESTION_PROVIDER).executes(LuaCommand::execute)
        )
    );
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
    
    context.getSource().sendFeedback(() ->
      Text.literal("Called /lua for script " + luascript.getIdentifier().toString() + " with source\n" + luascript.getSource())
    , false);

    return 1;
  }

}
