package com.homegui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.homegui.config.HomeGuiConfig;
import com.homegui.data.Home;
import com.homegui.data.HomeManager;
import com.homegui.lang.Lang;
import com.homegui.lang.Localization;
import com.homegui.net.HomeListPayload;
import com.homegui.util.ColorCodes;
import com.homegui.util.Permissions;
import com.homegui.util.Sounds;
import com.homegui.util.TeleportHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All of the mod's behaviour lives here. Both the chat commands and the buttons in the GUI
 * call into this class, so the two paths can never drift apart on limits or messages.
 */
public final class HomeService {
	/** Minecraft's formatting code prefix, rejected inside home names. */
	private static final char SECTION_SIGN = '§';

	private static final long MILLIS_PER_SECOND = 1000L;

	private static final Gson GSON = new Gson();

	/** Player id to the timestamp, in milliseconds, at which they may teleport again. */
	private static final Map<UUID, Long> COOLDOWNS = new ConcurrentHashMap<>();
	/** Teleports that are currently counting down. */
	private static final Map<UUID, PendingTeleport> PENDING = new ConcurrentHashMap<>();

	/** Mutable so the countdown can remember which second it last announced. */
	private static final class PendingTeleport {
		private final String homeName;
		private final long readyAtMillis;
		private final Vec3 startPosition;
		private int lastSecondShown = -1;

		private PendingTeleport(String homeName, long readyAtMillis, Vec3 startPosition) {
			this.homeName = homeName;
			this.readyAtMillis = readyAtMillis;
			this.startPosition = startPosition;
		}
	}

	private HomeService() {
	}

	// ------------------------------------------------------------------ set

	public static boolean setHome(ServerPlayer player, String rawName) {
		HomeGuiConfig config = HomeGuiConfig.get();
		String name = normalize(rawName);

		if (!isValidName(name)) {
			send(player, Lang.INVALID_NAME, config.maxHomeNameLength);
			return false;
		}

		if (!config.allowColorsInHomeNames) {
			name = ColorCodes.strip(name);
		}

		UUID id = player.getUUID();
		Home existing = HomeManager.find(id, name);

		if (existing != null && !config.allowOverwrite) {
			send(player, Lang.HOME_ALREADY_EXISTS, ColorCodes.parse(name));
			return false;
		}

		if (existing == null && !bypassLimits(player) && HomeManager.count(id) >= config.maxHomes) {
			send(player, Lang.LIMIT_REACHED, config.maxHomes);
			return false;
		}

		HomeManager.put(id, new Home(
				name,
				player.level().dimension().identifier().toString(),
				player.getX(), player.getY(), player.getZ(),
				player.getYRot(), player.getXRot()
		));

		send(player, Lang.HOME_SET, ColorCodes.parse(name), HomeManager.count(id), config.maxHomes);
		return true;
	}

	// --------------------------------------------------------------- delete

	public static boolean deleteHome(ServerPlayer player, String rawName) {
		String name = normalize(rawName);
		Home home = HomeManager.find(player.getUUID(), name);

		if (home == null) {
			send(player, Lang.HOME_NOT_FOUND, ColorCodes.parse(name));
			return false;
		}

		HomeManager.remove(player.getUUID(), name);
		send(player, Lang.HOME_DELETED, ColorCodes.parse(home.name));
		return true;
	}

	// --------------------------------------------------------------- rename

	/**
	 * Renames a home, keeping its position and its place in the list. Renaming onto a name
	 * that is already taken is refused even when overwriting is allowed, because it would
	 * silently destroy the other home.
	 */
	public static boolean renameHome(ServerPlayer player, String rawOldName, String rawNewName) {
		HomeGuiConfig config = HomeGuiConfig.get();
		String oldName = normalize(rawOldName);
		String newName = normalize(rawNewName);

		UUID id = player.getUUID();
		Home home = HomeManager.find(id, oldName);

		if (home == null) {
			send(player, Lang.HOME_NOT_FOUND, ColorCodes.parse(oldName));
			return false;
		}

		if (!config.allowColorsInHomeNames) {
			newName = ColorCodes.strip(newName);
		}

		if (!isValidName(newName)) {
			send(player, Lang.INVALID_NAME, config.maxHomeNameLength);
			return false;
		}

		Home clash = HomeManager.find(id, newName);

		if (clash != null && clash != home) {
			send(player, Lang.HOME_ALREADY_EXISTS, ColorCodes.parse(newName));
			return false;
		}

		String previousName = home.name;
		HomeManager.rename(id, oldName, newName);
		send(player, Lang.HOME_RENAMED, ColorCodes.parse(previousName), ColorCodes.parse(newName));
		return true;
	}

	// ------------------------------------------------------------- teleport

	public static boolean teleportHome(ServerPlayer player, String rawName) {
		HomeGuiConfig config = HomeGuiConfig.get();
		String name = normalize(rawName);
		Home home = HomeManager.find(player.getUUID(), name);

		if (home == null) {
			send(player, Lang.HOME_NOT_FOUND, ColorCodes.parse(name));
			return false;
		}

		ServerLevel level = resolveLevel(player.level().getServer(), home.dimension);

		if (level == null) {
			send(player, Lang.DIMENSION_MISSING);
			return false;
		}

		if (!config.allowCrossDimension && level != player.level()) {
			send(player, Lang.CROSS_DIMENSION_DENIED);
			return false;
		}

		boolean privileged = bypassLimits(player);

		if (!privileged) {
			long now = System.currentTimeMillis();
			long readyAt = COOLDOWNS.getOrDefault(player.getUUID(), 0L);

			if (now < readyAt) {
				send(player, Lang.COOLDOWN, secondsUntil(readyAt, now));
				return false;
			}
		}

		if (config.teleportWarmupSeconds > 0 && !privileged) {
			PENDING.put(player.getUUID(), new PendingTeleport(
					home.name,
					System.currentTimeMillis() + config.teleportWarmupSeconds * MILLIS_PER_SECOND,
					player.position()
			));
			send(player, Lang.WARMUP_STARTED, ColorCodes.parse(home.name));
			return true;
		}

		doTeleport(player, home, level);
		return true;
	}

	private static void doTeleport(ServerPlayer player, Home home, ServerLevel level) {
		HomeGuiConfig config = HomeGuiConfig.get();

		TeleportHelper.teleport(player, level, home.x, home.y, home.z, home.yaw, home.pitch);

		if (config.teleportCooldownSeconds > 0 && !bypassLimits(player)) {
			COOLDOWNS.put(player.getUUID(),
					System.currentTimeMillis() + config.teleportCooldownSeconds * MILLIS_PER_SECOND);
		}

		Sounds.playTo(player, config.teleportSound,
				config.teleportSoundVolume, config.teleportSoundPitch);
		send(player, Lang.HOME_TELEPORTED, ColorCodes.parse(home.name));
	}

	/** Called every server tick to advance the teleports that are counting down. */
	public static void tick(MinecraftServer server) {
		if (PENDING.isEmpty()) {
			return;
		}

		HomeGuiConfig config = HomeGuiConfig.get();
		double tolerance = config.warmupMoveTolerance * config.warmupMoveTolerance;
		long now = System.currentTimeMillis();

		PENDING.entrySet().removeIf(entry -> {
			ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());

			if (player == null) {
				return true;
			}

			PendingTeleport pending = entry.getValue();

			if (config.cancelWarmupOnMove
					&& player.position().distanceToSqr(pending.startPosition) > tolerance) {
				send(player, Lang.WARMUP_CANCELLED);
				return true;
			}

			long remaining = pending.readyAtMillis - now;

			if (remaining > 0) {
				announceCountdown(player, pending, remaining, config);
				return false;
			}

			Home home = HomeManager.find(player.getUUID(), pending.homeName);

			if (home == null) {
				send(player, Lang.HOME_NOT_FOUND, ColorCodes.parse(pending.homeName));
				return true;
			}

			ServerLevel level = resolveLevel(server, home.dimension);

			if (level == null) {
				send(player, Lang.DIMENSION_MISSING);
			} else {
				doTeleport(player, home, level);
			}

			return true;
		});
	}

	/**
	 * Shows the remaining seconds on the action bar and ticks the countdown sound, but only
	 * when the number actually changed rather than on every one of the twenty ticks a second.
	 */
	private static void announceCountdown(ServerPlayer player, PendingTeleport pending,
			long remainingMillis, HomeGuiConfig config) {
		int seconds = (int) ((remainingMillis + MILLIS_PER_SECOND - 1) / MILLIS_PER_SECOND);

		if (seconds == pending.lastSecondShown) {
			return;
		}

		pending.lastSecondShown = seconds;
		player.sendSystemMessage(Localization.message(player, Lang.WARMUP_COUNTDOWN, seconds), true);
		Sounds.playTo(player, config.warmupTickSound,
				config.warmupTickSoundVolume, config.warmupTickSoundPitch);
	}

	/** Drops the per player state kept in memory once they disconnect. */
	public static void forget(UUID playerId) {
		COOLDOWNS.remove(playerId);
		PENDING.remove(playerId);
	}

	// ------------------------------------------------------------------ list

	/** Prints the home list in chat, used when the player's client does not have the mod. */
	public static void listHomes(ServerPlayer player) {
		List<Home> homes = HomeManager.listOf(player.getUUID());

		if (homes.isEmpty()) {
			send(player, Lang.NO_HOMES);
			return;
		}

		send(player, Lang.LIST_HEADER, homes.size(), HomeGuiConfig.get().maxHomes);

		for (Home home : homes) {
			send(player, Lang.LIST_ENTRY,
					ColorCodes.parse(home.name),
					shortDimension(home.dimension),
					coordinate(home.x), coordinate(home.y), coordinate(home.z));
		}
	}

	// ------------------------------------------------------------------- gui

	/** True when the player's client has the mod and can therefore receive the GUI packet. */
	public static boolean canOpenGui(ServerPlayer player) {
		return ServerPlayNetworking.canSend(player, HomeListPayload.TYPE);
	}

	/** Asks the client to open the home screen. */
	public static void openGui(ServerPlayer player) {
		sendHomeList(player, true);
	}

	/** Pushes a fresh list to the client; the screen only reacts if it is already open. */
	public static void sendHomeList(ServerPlayer player) {
		sendHomeList(player, false);
	}

	private static void sendHomeList(ServerPlayer player, boolean open) {
		if (!canOpenGui(player)) {
			return;
		}

		HomeGuiConfig config = HomeGuiConfig.get();

		JsonObject root = new JsonObject();
		root.addProperty("open", open);
		root.addProperty("maxHomes", config.maxHomes);
		root.addProperty("perPage", config.guiEntriesPerPage);
		root.addProperty("maxNameLength", config.nameInputLimit());
		root.addProperty("currentDimension", player.level().dimension().identifier().toString());

		JsonArray array = new JsonArray();

		for (Home home : HomeManager.listOf(player.getUUID())) {
			JsonObject entry = new JsonObject();
			entry.addProperty("name", home.name);
			entry.addProperty("dimension", home.dimension);
			entry.addProperty("x", home.x);
			entry.addProperty("y", home.y);
			entry.addProperty("z", home.z);
			array.add(entry);
		}

		root.add("homes", array);

		ServerPlayNetworking.send(player, new HomeListPayload(GSON.toJson(root)));
	}

	// --------------------------------------------------------------- helpers

	private static boolean bypassLimits(ServerPlayer player) {
		HomeGuiConfig config = HomeGuiConfig.get();
		return config.opBypassLimits && Permissions.has(player.permissions(), config.opPermissionLevel);
	}

	/** Falls back to the configured default name when the player did not type one. */
	public static String normalize(String raw) {
		if (raw == null) {
			return HomeGuiConfig.get().defaultHomeName;
		}

		String trimmed = raw.trim();
		return trimmed.isEmpty() ? HomeGuiConfig.get().defaultHomeName : trimmed;
	}

	/**
	 * Length and content are checked against the name as it reads on screen, so colour markup
	 * does not eat into the player's character budget.
	 */
	private static boolean isValidName(String name) {
		HomeGuiConfig config = HomeGuiConfig.get();
		String plain = ColorCodes.strip(name);

		if (plain.isBlank() || plain.length() > config.maxHomeNameLength) {
			return false;
		}

		if (name.length() > config.nameInputLimit()) {
			return false;
		}

		for (int i = 0; i < plain.length(); i++) {
			char c = plain.charAt(i);

			// Spaces are fine. A section sign left over after the codes were stripped is not
			// part of a valid code, and control characters would corrupt the chat listing.
			if (c == SECTION_SIGN || Character.isISOControl(c)) {
				return false;
			}
		}

		return true;
	}

	private static ServerLevel resolveLevel(MinecraftServer server, String dimensionId) {
		if (server == null || dimensionId == null) {
			return null;
		}

		Identifier id = Identifier.tryParse(dimensionId);

		if (id == null) {
			return null;
		}

		ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, id);
		return server.getLevel(key);
	}

	/** {@code minecraft:the_nether} reads better as {@code the_nether} in a message. */
	private static String shortDimension(String dimensionId) {
		if (dimensionId == null) {
			return "";
		}

		int separator = dimensionId.indexOf(':');
		return separator >= 0 ? dimensionId.substring(separator + 1) : dimensionId;
	}

	private static String coordinate(double value) {
		return String.format(Locale.ROOT, "%.0f", value);
	}

	private static long secondsUntil(long readyAt, long now) {
		return Math.max(1L, (readyAt - now + MILLIS_PER_SECOND - 1) / MILLIS_PER_SECOND);
	}

	private static void send(ServerPlayer player, String key, Object... args) {
		player.sendSystemMessage(Localization.prefixed(player, key, args));
	}
}
