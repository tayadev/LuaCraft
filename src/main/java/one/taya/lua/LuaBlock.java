package one.taya.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;

public class LuaBlock {
  public static LuaValue create(BlockState block) {
    LuaTable blockTable = new LuaTable(0,30);

    blockTable.set("id", Registries.BLOCK.getId(block.getBlock()).toString());
    blockTable.set("hardness", block.getHardness(null, null));

    return blockTable;
  }
}