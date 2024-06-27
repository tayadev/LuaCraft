package one.taya;

import java.util.Collection;
import java.util.Optional;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import net.minecraft.util.Identifier;

import static one.taya.LuaCraft.LOGGER;

public class LuaScriptManager {

  // akin to net.minecraft.server.function.FunctionManager

  private LuaScriptLoader loader;

  public LuaScriptManager(LuaScriptLoader loader) {
    this.loader = loader;
  }

  public Iterable<Identifier> getLuaScriptTags() {
    return loader.getTags();
  }

  public Iterable<Identifier> getAllLuaScripts() {
    return loader.getLuaScripts().keySet();
  }

  public Optional<LuaScript> getLuaScript(Identifier id) {
    return loader.get(id);
  }

  public Collection<LuaScript> getTag(Identifier id) {
    return loader.getTagOrEmpty(id);
  }

  public void execute(LuaScript script) throws LuaError {
    Globals luaVM = JsePlatform.standardGlobals();
    LuaValue chunk = luaVM.load(script.getSource());
    chunk.call();
  }

}
