package one.taya.lua;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import one.taya.Main;

public class MinecraftLib extends TwoArgFunction {

  public MinecraftLib() {}

  public LuaValue call(LuaValue modname, LuaValue env) {
    LuaTable minecraft = new LuaTable(0,30);
		minecraft.set("say", new say());
    minecraft.set("onTick", new onTick());
    minecraft.set("getWorld", new getWorld());
    env.set("minecraft", minecraft);
		if (!env.get("package").isnil()) env.get("package").get("loaded").set("minecraft", minecraft);
		return minecraft;
  }

 class say extends OneArgFunction {
    public LuaValue call(LuaValue x) {
      String message = x.strvalue().tojstring();

      Main.SERVER.getPlayerManager().broadcast(Text.of(message), false);

      return LuaValue.TRUE;
    }
  }

  static class getWorld extends OneArgFunction {
    public LuaValue call(LuaValue x) {
      String id = x.strvalue().tojstring();

      RegistryKey<World> worldKey = Main.SERVER.getWorldRegistryKeys().stream()
        .filter(key -> key.getValue().toString().equals(id))
        .findFirst()
        .orElse(null);

      ServerWorld world = Main.SERVER.getWorld(worldKey);

      return LuaWorld.create(world);
    }
  }

  public static List<LuaValue> onTickCallbacks = new ArrayList<LuaValue>();

  static class onTick extends OneArgFunction {
    public LuaValue call(LuaValue x) {
      // add x to onTickCallbacks
      onTickCallbacks.add(x);
      return LuaValue.TRUE;
    }
  }
}