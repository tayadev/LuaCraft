package one.taya;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class LuaCommand {

  // Register the /lua command
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("lua")
        .requires(source -> source.hasPermissionLevel(2))
        .then(CommandManager.argument("id", IdentifierArgumentType.identifier())
          .executes(LuaCommand::executeLuaScript));

    dispatcher.register(command);
  }

  // Execute the Lua code
  private static int executeLuaScript(CommandContext<ServerCommandSource> context) {
    Identifier id = IdentifierArgumentType.getIdentifier(context, "id");

    Main.ScriptManager.runScript(id);

    return 1; // Return 1 as a placeholder for the command result
  }
}
