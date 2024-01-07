package one.taya;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;
import one.taya.lua.MinecraftLib;

public class ReloadListener implements IdentifiableResourceReloadListener, SynchronousResourceReloader {

  @Override
  public void reload(ResourceManager resourceManager) {

    Main.LOGGER.info("Loading Lua scripts");
    Main.ScriptManager = new ScriptManager();
    MinecraftLib.onTickCallbacks.clear();

    for(Map.Entry<Identifier, Resource> entry : resourceManager.findResources("lua", path -> true).entrySet()) {
      Identifier id = entry.getKey();
      Resource resource = entry.getValue();

      Main.LOGGER.info("Loading Lua script: " + id.toString());
      // load script
      try (BufferedReader reader = resource.getReader()) {

        // remove lua/ prefix and .lua suffix from id
        String scriptName = id.getPath().replace("lua/", "").replace(".lua", "");

        Main.ScriptManager.loadScript(new Identifier(id.getNamespace(), scriptName), reader);

      } catch (IOException e) {
        Main.LOGGER.error("Error loading Lua script: " + id.toString());
      }
    }

    Main.ScriptManager.runInitScripts();
  }

  @Override
  public Identifier getFabricId() {
    return new Identifier("luacraft", "lua");
  }
  
}
