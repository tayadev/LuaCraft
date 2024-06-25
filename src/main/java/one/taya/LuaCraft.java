package one.taya;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class LuaCraft implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("luacraft");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
  }
}
