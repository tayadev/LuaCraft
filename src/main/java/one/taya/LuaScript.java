package one.taya;

import net.minecraft.util.Identifier;

public class LuaScript {

  private Identifier identifier;
  private String source;

  public LuaScript(Identifier identifier, String source) {
    this.identifier = identifier;
    this.source = source;
  }

  public Identifier getIdentifier() {
    return this.identifier;
  }

  public String getSource() {
    return this.source;
  }
  
}
