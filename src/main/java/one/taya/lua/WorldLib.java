package one.taya.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import one.taya.Main;

public class WorldLib extends TwoArgFunction {

  public WorldLib() {}

  public LuaValue call(LuaValue modname, LuaValue env) {
    LuaTable minecraft = new LuaTable(0,30);
		minecraft.set("setblock", new setblock());
    env.set("world", minecraft);
		if (!env.get("package").isnil()) env.get("package").get("loaded").set("world", minecraft);
		return minecraft;
  }

  static class setblock extends TwoArgFunction {
    public LuaValue call(LuaValue lpos, LuaValue lblock) {
      BlockPos pos = new BlockPos(lpos.get("x").toint(), lpos.get("y").toint(), lpos.get("z").toint());

      Registries.BLOCK.getOrEmpty(Identifier.tryParse(lblock.tostring().tojstring())).ifPresent(block -> {
        Main.SERVER.getOverworld().setBlockState(pos, block.getDefaultState());
      });

      return LuaValue.TRUE;
    }
  }
}