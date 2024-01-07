package one.taya;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import net.minecraft.util.Identifier;

import one.taya.lua.MinecraftLib;
import one.taya.lua.WorldLib;

public class ScriptManager {

  Globals vm;
  Map<Identifier, LuaValue> scripts;

  public ScriptManager() {
    vm = JsePlatform.standardGlobals();
    scripts = new HashMap<Identifier, LuaValue>();

    vm.load(new MinecraftLib());
    vm.load(new WorldLib());

    vm.finder = new LuaResourceFinder();
    // vm.set("require", new require());
  }

  public void loadScript(Identifier id, Reader source) {
    LuaValue chunk = vm.load(source, id.getPath());
    scripts.put(id, chunk);
  }

  public void runScript(Identifier id) {
    Main.LOGGER.info("Running Lua script: " + id.toString());

    if (!scripts.containsKey(id)) {
      Main.LOGGER.error("Lua script not found: " + id.toString());
      return;
    }

    LuaValue chunk = scripts.get(id);
    try {
      chunk.call();
    } catch (Exception e) {
      Main.LOGGER.error("Error running Lua script: " + id.toString());
      Main.LOGGER.error(e.toString());
    }
  }

  public LuaValue getScript(Identifier id) {
    return scripts.get(id);
  }

  public void runInitScripts() {
    // run all scripts with path init.lua
    for(Map.Entry<Identifier, LuaValue> entry : scripts.entrySet()) {
      Identifier id = entry.getKey();
      LuaValue chunk = entry.getValue();

      if (id.getPath().equals("init")) {
        Main.LOGGER.info("Running init script: " + id.toString());
        try {
          chunk.call();
        } catch (Exception e) {
          Main.LOGGER.error("Error running init script: " + id.toString());
          Main.LOGGER.error(e.toString());
        }
      }
    }
  }
}
