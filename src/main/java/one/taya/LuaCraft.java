package one.taya;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;

public class LuaCraft implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("luacraft");
	public static final String MODID = "luacraft";

	public static final LuaScriptLoader LUA_SCRIPT_LOADER = new LuaScriptLoader();
	public static final LuaScriptManager LUA_SCRIPT_MANAGER = new LuaScriptManager(LUA_SCRIPT_LOADER);

	public static MinecraftServer SERVER;

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			LuaCommand.register(dispatcher);
		});

		// Register LuaScriptLoader as a resource reloader
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(LUA_SCRIPT_LOADER);

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			LuaCraft.SERVER = server;
		});

		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			LuaCraft.SERVER = null;
		});

  }
}
