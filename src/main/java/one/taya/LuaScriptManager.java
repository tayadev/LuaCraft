package one.taya;

import java.util.Collection;
import java.util.Optional;

import net.minecraft.util.Identifier;

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

}
