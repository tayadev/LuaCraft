package one.taya.lua;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import one.taya.Main;

public class MinecraftLib extends TwoArgFunction {

  public MinecraftLib() {}

  public LuaValue call(LuaValue modname, LuaValue env) {
    LuaTable minecraft = new LuaTable(0,30);
		minecraft.set("test", new test());
		minecraft.set("say", new say());
    minecraft.set("onTick", new onTick());
    env.set("minecraft", minecraft);
		if (!env.get("package").isnil()) env.get("package").get("loaded").set("minecraft", minecraft);
		return minecraft;
  }

  static class test extends ZeroArgFunction {
    public LuaValue call() {
      return valueOf("hello, world");
    }
  }

  static class say extends OneArgFunction {
    public LuaValue call(LuaValue x) {
      // send x to mc chat
      String message = x.strvalue().tojstring();

      Main.SERVER.getPlayerManager().broadcast(Text.of(message), false);

      return LuaValue.TRUE;
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