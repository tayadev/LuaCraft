package one.taya;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import one.taya.lua.MinecraftLib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("luacraft");
	public static MinecraftServer SERVER;
	public static ScriptManager ScriptManager = new ScriptManager();

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			LuaCommand.register(dispatcher);
		});
		
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			SERVER = server;
			ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ReloadListener());
			ScriptManager.runInitScripts();
		});

		ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
			MinecraftLib.onTickCallbacks.forEach(callback -> {
				callback.call();
			});
		});

	}

}