package com.homegui.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.homegui.HomeGui;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reads and writes each player's homes to {@code <world>/homegui/<uuid>.json}.
 * The data belongs to a single world and is not shared between saves.
 */
public final class HomeManager {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Type MAP_TYPE = new TypeToken<LinkedHashMap<String, Home>>() {
	}.getType();

	private static final Map<UUID, LinkedHashMap<String, Home>> CACHE = new ConcurrentHashMap<>();
	private static volatile Path rootDir;

	private HomeManager() {
	}

	public static void init(MinecraftServer server) {
		rootDir = server.getWorldPath(LevelResource.ROOT).resolve(HomeGui.MOD_ID);
		CACHE.clear();

		try {
			Files.createDirectories(rootDir);
		} catch (Exception e) {
			HomeGui.LOGGER.error("Could not create the home storage directory", e);
		}
	}

	public static void shutdown() {
		saveAll();
		CACHE.clear();
		rootDir = null;
	}

	// ------------------------------------------------------------------ query

	/** The player's homes keyed by lower cased name. Never null. */
	public static LinkedHashMap<String, Home> homesOf(UUID playerId) {
		return CACHE.computeIfAbsent(playerId, HomeManager::read);
	}

	public static List<Home> listOf(UUID playerId) {
		return new ArrayList<>(homesOf(playerId).values());
	}

	public static Home find(UUID playerId, String name) {
		if (name == null) {
			return null;
		}

		return homesOf(playerId).get(name.toLowerCase(Locale.ROOT));
	}

	public static int count(UUID playerId) {
		return homesOf(playerId).size();
	}

	// ----------------------------------------------------------------- mutate

	public static void put(UUID playerId, Home home) {
		homesOf(playerId).put(home.key(), home);
		save(playerId);
	}

	/**
	 * Changes a home's name while keeping its position in the list, so renaming does not
	 * shuffle the player's GUI around.
	 */
	public static void rename(UUID playerId, String oldName, String newName) {
		LinkedHashMap<String, Home> homes = homesOf(playerId);
		String oldKey = oldName.toLowerCase(Locale.ROOT);
		Home home = homes.get(oldKey);

		if (home == null) {
			return;
		}

		home.name = newName;

		if (home.key().equals(oldKey)) {
			// Only the capitalisation changed, the entry stays where it is.
			save(playerId);
			return;
		}

		LinkedHashMap<String, Home> reordered = new LinkedHashMap<>();

		for (Map.Entry<String, Home> entry : homes.entrySet()) {
			if (entry.getKey().equals(oldKey)) {
				reordered.put(home.key(), home);
			} else {
				reordered.put(entry.getKey(), entry.getValue());
			}
		}

		homes.clear();
		homes.putAll(reordered);
		save(playerId);
	}

	public static boolean remove(UUID playerId, String name) {
		if (name == null) {
			return false;
		}

		boolean removed = homesOf(playerId).remove(name.toLowerCase(Locale.ROOT)) != null;

		if (removed) {
			save(playerId);
		}

		return removed;
	}

	// -------------------------------------------------------------- storage

	private static LinkedHashMap<String, Home> read(UUID playerId) {
		Path path = fileOf(playerId);

		if (path == null || !Files.exists(path)) {
			return new LinkedHashMap<>();
		}

		try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			LinkedHashMap<String, Home> loaded = GSON.fromJson(reader, MAP_TYPE);
			return loaded != null ? loaded : new LinkedHashMap<>();
		} catch (Exception e) {
			HomeGui.LOGGER.error("Could not read the homes of {}", playerId, e);
			return new LinkedHashMap<>();
		}
	}

	public static void save(UUID playerId) {
		Path path = fileOf(playerId);

		if (path == null) {
			return;
		}

		try {
			Files.createDirectories(path.getParent());
			Files.writeString(path, GSON.toJson(homesOf(playerId), MAP_TYPE), StandardCharsets.UTF_8);
		} catch (Exception e) {
			HomeGui.LOGGER.error("Could not write the homes of {}", playerId, e);
		}
	}

	public static void saveAll() {
		for (UUID playerId : CACHE.keySet()) {
			save(playerId);
		}
	}

	/** Drops the cache entry when a player leaves so memory is not held for nothing. */
	public static void unload(UUID playerId) {
		save(playerId);
		CACHE.remove(playerId);
	}

	private static Path fileOf(UUID playerId) {
		Path dir = rootDir;
		return dir == null ? null : dir.resolve(playerId + ".json");
	}
}
