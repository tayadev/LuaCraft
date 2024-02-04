package one.taya.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LuaWorld {
  public static LuaValue create(ServerWorld world) {
    LuaTable worldTable = new LuaTable(0,30);

    worldTable.set("id", world.getRegistryKey().getValue().toString());

    worldTable.set("setBlock", new ThreeArgFunction() {
      public LuaValue call(LuaValue self, LuaValue pos, LuaValue block) {
        BlockPos blockPos = new BlockPos(pos.get("x").toint(), pos.get("y").toint(), pos.get("z").toint());
        Identifier blockId = Identifier.tryParse(block.tostring().tojstring());
        Registries.BLOCK.getOrEmpty(blockId).ifPresent(registeredblock -> {
          world.setBlockState(blockPos, registeredblock.getDefaultState());
        });
        return self;
      }
    });

    worldTable.set("getBlock", new ThreeArgFunction() {
      public LuaValue call(LuaValue self, LuaValue pos, LuaValue block) {
        BlockPos blockPos = new BlockPos(pos.get("x").toint(), pos.get("y").toint(), pos.get("z").toint());
        return LuaBlock.create(world.getBlockState(blockPos));
      }
    });

    return worldTable;
  }
}
