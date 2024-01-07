package one.taya;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.luaj.vm2.lib.ResourceFinder;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class LuaResourceFinder implements ResourceFinder {

  @Override
  public InputStream findResource(String filename) {
    ResourceManager resourceManager = Main.SERVER.getResourceManager();

    boolean hasNamespace = filename.contains(":");
    Identifier id = hasNamespace
      ? new Identifier(filename.split(":")[0], filename.split(":")[1])
      : new Identifier("minecraft", filename);

    for(Map.Entry<Identifier, Resource> entry : resourceManager.findResources("lua", path -> true).entrySet()) {
      Identifier _id = entry.getKey();
      Resource resource = entry.getValue();

      Identifier currentIdentifier = new Identifier(_id.getNamespace(), _id.getPath().replace("lua/", ""));
      
      if (id.equals(currentIdentifier)) {
        try {
          return resource.getInputStream();
        } catch (IOException e) {
          return null;
        }
      }
    }

    return null;
  }
}
