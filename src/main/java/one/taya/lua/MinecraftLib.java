package one.taya.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minecraft.text.Text;
import one.taya.LuaCraft;

public class MinecraftLib extends TwoArgFunction {

  public MinecraftLib() {}

  public LuaValue call(LuaValue modname, LuaValue env) {
    LuaValue library = tableOf();
    library.set( "say", new say() );
    env.set( "minecraft", library );
		if (!env.get("package").isnil()) env.get("package").get("loaded").set("minecraft", library);
    return library;
  }

  static class say extends OneArgFunction {
    public LuaValue call(LuaValue x) {

      String message = x.checkjstring();

      // send message to all players
      LuaCraft.SERVER.getPlayerManager().broadcast(Text.of(message), false);

      return LuaValue.NIL;
    }
  }

}
