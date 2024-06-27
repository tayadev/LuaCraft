package one.taya;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

public class LuaScriptLoader implements IdentifiableResourceReloadListener {

  // akin to net.minecraft.server.function.FunctionLoader

	private static final Logger LOGGER = LogUtils.getLogger();
	public static final RegistryKey<Registry<LuaScript>> LUASCRIPT_REGISTRY_KEY = RegistryKey.ofRegistry(
		Identifier.ofVanilla("lua")
	);
  private static final ResourceFinder FINDER = new ResourceFinder(RegistryKeys.getPath(LUASCRIPT_REGISTRY_KEY), ".lua");
  private volatile Map<Identifier, LuaScript> luascripts = ImmutableMap.of();
	private final TagGroupLoader<LuaScript> tagLoader = new TagGroupLoader<>(this::get, RegistryKeys.getTagPath(LUASCRIPT_REGISTRY_KEY));
	private volatile Map<Identifier, Collection<LuaScript>> tags = Map.of();


  public Optional<LuaScript> get(Identifier id) {
    return Optional.ofNullable(this.luascripts.get(id));
  }

  public Map<Identifier, LuaScript> getLuaScripts() {
    return this.luascripts;
  }

	public Collection<LuaScript> getTagOrEmpty(Identifier id) {
		return (Collection<LuaScript>)this.tags.getOrDefault(id, List.of());
	}

	public Iterable<Identifier> getTags() {
		return this.tags.keySet();
	}

  @Override
  public CompletableFuture<Void> reload(
    Synchronizer synchronizer,
    ResourceManager manager,
    Profiler prepareProfiler,
    Profiler applyProfiler,
    Executor prepareExecutor,
    Executor applyExecutor
  ) {

    CompletableFuture<Map<Identifier, List<TagGroupLoader.TrackedEntry>>> completableFuture = CompletableFuture.supplyAsync(
      () -> this.tagLoader.loadTags(manager), prepareExecutor
    );

    CompletableFuture<Map<Identifier, CompletableFuture<LuaScript>>> completableFuture2 = CompletableFuture.supplyAsync(
      () -> FINDER.findResources(manager), prepareExecutor
    )
    .thenCompose(
      luascripts -> {
        Map<Identifier, CompletableFuture<LuaScript>> map = Maps.<Identifier, CompletableFuture<LuaScript>>newHashMap();

        for(Entry<Identifier, Resource> entry : luascripts.entrySet()) {
          Identifier identifier = (Identifier)entry.getKey();
          Identifier identifier2 = FINDER.toResourceId(identifier);
          map.put(identifier2, CompletableFuture.supplyAsync(() -> {
            String content = readLines((Resource) entry.getValue());
            return new LuaScript(identifier2, content);
          }, prepareExecutor));
        }

        CompletableFuture<?>[] completableFutures = (CompletableFuture<?>[])map.values().toArray(new CompletableFuture[0]);
        return CompletableFuture.allOf(completableFutures).handle((unused, ex) -> map);
      }
    );

    return completableFuture.thenCombine(completableFuture2, Pair::of)
      .thenCompose(synchronizer::whenPrepared)
      .thenAcceptAsync(
        intermediate -> {
					Map<Identifier, CompletableFuture<LuaScript>> map = (Map<Identifier, CompletableFuture<LuaScript>>)intermediate.getSecond();
					Builder<Identifier, LuaScript> builder = ImmutableMap.builder();
					map.forEach((id, luascriptFuture) -> luascriptFuture.handle((luascript, ex) -> {
							if (ex != null) {
								LOGGER.error("Failed to load lua script {}", id, ex);
							} else {
								builder.put(id, luascript);
							}

							return null;
						}).join());
					this.luascripts = builder.build();
					this.tags = this.tagLoader.buildGroup((Map<Identifier, List<TagGroupLoader.TrackedEntry>>)intermediate.getFirst());
				},
				applyExecutor
      );
  }


	private static String readLines(Resource resource) {
		try {
			BufferedReader bufferedReader = resource.getReader();

			String var2 = "";
			try {
				var2 = bufferedReader.lines().reduce(var2, (string, string2) -> {
					return string + string2 + "\n";
				});
			} catch (Throwable var5) {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (Throwable var4) {
						var5.addSuppressed(var4);
					}
				}

				throw var5;
			}

			if (bufferedReader != null) {
				bufferedReader.close();
			}

			return var2;
		} catch (IOException var6) {
			throw new CompletionException(var6);
		}
	}

  @Override
  public Identifier getFabricId() {
    return Identifier.of("luacraft", "luascript");
  }

}