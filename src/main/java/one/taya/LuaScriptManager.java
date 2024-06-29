package one.taya;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import one.taya.lua.MinecraftLib;

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

  private Globals createLuaVM() {
    Globals vm = JsePlatform.standardGlobals(); // FIXME: this might be dangerous

    // Load the minecraft library as a global called "mc"
    vm.load(new MinecraftLib());
    // vm.finder = new LuaResourceFinder(); // TODO: add custom reource finder

    return vm;
  }

  public void execute(LuaScript script) throws LuaError {
    Map<String, Globals> luaVMs = loader.luaVMs;

    // Get the LuaVM for the script's datapack
    Globals luaVM = luaVMs.get(script.getIdentifier().getNamespace());
    if (luaVM == null) {
      luaVM = createLuaVM();
      luaVMs.put(script.getIdentifier().getNamespace(), luaVM);
    }

    LuaValue chunk = luaVM.load(script.getSource());

    // Limit script execution time to 1 second
    
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<?> future = executor.submit(() -> {
      chunk.call();
    });

  try {
    future.get(1, TimeUnit.SECONDS);
  } catch (TimeoutException | InterruptedException | ExecutionException e) {
    throw new LuaError(e);
  } finally {
    executor.shutdownNow();
  }



  }

}
