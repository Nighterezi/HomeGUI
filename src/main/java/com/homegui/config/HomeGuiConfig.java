package com.homegui.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homegui.HomeGui;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Mod configuration, stored at {@code <gamedir>/config/homegui.json}.
 * The file is written with these defaults the first time the mod starts.
 *
 * <p>Wording is not configured here. Messages live in the language files under
 * {@code assets/homegui/lang} so that each player sees them in their own language.
 */
public class HomeGuiConfig {
	/** Upper bound for {@link #guiEntriesPerPage}, so a bad value cannot produce an unusable screen. */
	private static final int MAX_ENTRIES_PER_PAGE = 10;

	/** Head room for the colour codes wrapped around a name of the maximum visible length. */
	private static final int COLOR_MARKUP_ALLOWANCE = 64;

	/** Above 1 the sound simply carries further, so there is no point going very high. */
	private static final float MAX_SOUND_VOLUME = 4.0F;
	private static final float MIN_SOUND_PITCH = 0.5F;
	private static final float MAX_SOUND_PITCH = 2.0F;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static HomeGuiConfig instance = new HomeGuiConfig();

	// --------------------------------------------------------------- options

	/** Maximum number of homes a player may own. */
	public int maxHomes = 3;

	/** Home name used by /sethome, /home and /delhome when no name is given. */
	public String defaultHomeName = "home";

	/** Seconds a player must wait between teleports. 0 disables the cooldown. */
	public int teleportCooldownSeconds = 0;

	/** Seconds the player has to stand still before the teleport happens. 0 teleports instantly. */
	public int teleportWarmupSeconds = 0;

	/** Whether moving during the warmup cancels the teleport. */
	public boolean cancelWarmupOnMove = true;

	/** How far a player may drift during the warmup before it counts as moving, in blocks. */
	public double warmupMoveTolerance = 0.5D;

	/** Whether players may teleport to a home that is in another dimension. */
	public boolean allowCrossDimension = true;

	/** Whether /sethome may overwrite an existing home with the same name. */
	public boolean allowOverwrite = true;

	/** Whether operators ignore the home limit and the teleport cooldown. */
	public boolean opBypassLimits = true;

	/** Permission level treated as operator, from 0 (everyone) to 4 (owner). */
	public int opPermissionLevel = 2;

	/** Maximum length of a home name, counted without any colour markup. */
	public int maxHomeNameLength = 24;

	/**
	 * Whether players may colour their home names with {@code &} or {@code §} codes and
	 * {@code &#RRGGBB}. When off, any markup is quietly removed instead of rejected.
	 */
	public boolean allowColorsInHomeNames = true;

	/** How many homes one page of the GUI shows. */
	public int guiEntriesPerPage = 6;

	/** Whether /home without a name opens the GUI for players who have the mod installed. */
	public boolean openGuiOnBareHomeCommand = true;

	/** Sound id played once per second while the warmup counts down. Leave empty to disable. */
	public String warmupTickSound = "minecraft:block.note_block.hat";

	public float warmupTickSoundVolume = 0.6F;

	public float warmupTickSoundPitch = 1.4F;

	/** Sound id played when the player arrives at a home. Leave empty to disable. */
	public String teleportSound = "minecraft:entity.enderman.teleport";

	public float teleportSoundVolume = 0.7F;

	public float teleportSoundPitch = 1.0F;

	// -------------------------------------------------------------- load/save

	public static HomeGuiConfig get() {
		return instance;
	}

	/**
	 * Longest name accepted as typed, markup included. {@link #maxHomeNameLength} counts only
	 * the visible characters, so the input needs extra room for the codes around them.
	 */
	public int nameInputLimit() {
		return maxHomeNameLength + COLOR_MARKUP_ALLOWANCE;
	}

	private static Path configPath() {
		return FabricLoader.getInstance().getConfigDir().resolve(HomeGui.MOD_ID + ".json");
	}

	public static void load() {
		Path path = configPath();

		if (!Files.exists(path)) {
			instance = new HomeGuiConfig();
			save();
			return;
		}

		try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			HomeGuiConfig loaded = GSON.fromJson(reader, HomeGuiConfig.class);
			instance = loaded != null ? loaded : new HomeGuiConfig();
			instance.sanitize();
		} catch (Exception e) {
			HomeGui.LOGGER.error("Could not read config, falling back to the defaults", e);
			instance = new HomeGuiConfig();
		}

		// Write the file back so options added by an update appear with their defaults.
		save();
	}

	public static void save() {
		Path path = configPath();

		try {
			Files.createDirectories(path.getParent());
			Files.writeString(path, GSON.toJson(instance), StandardCharsets.UTF_8);
		} catch (IOException e) {
			HomeGui.LOGGER.error("Could not write config", e);
		}
	}

	private void sanitize() {
		if (maxHomes < 1) maxHomes = 1;
		if (maxHomeNameLength < 1) maxHomeNameLength = 1;
		if (teleportCooldownSeconds < 0) teleportCooldownSeconds = 0;
		if (teleportWarmupSeconds < 0) teleportWarmupSeconds = 0;
		if (warmupMoveTolerance < 0) warmupMoveTolerance = 0;
		if (guiEntriesPerPage < 1) guiEntriesPerPage = 1;
		if (guiEntriesPerPage > MAX_ENTRIES_PER_PAGE) guiEntriesPerPage = MAX_ENTRIES_PER_PAGE;
		if (defaultHomeName == null || defaultHomeName.isBlank()) defaultHomeName = "home";
		if (warmupTickSound == null) warmupTickSound = "";
		if (teleportSound == null) teleportSound = "";

		warmupTickSoundVolume = clampVolume(warmupTickSoundVolume);
		teleportSoundVolume = clampVolume(teleportSoundVolume);
		warmupTickSoundPitch = clampPitch(warmupTickSoundPitch);
		teleportSoundPitch = clampPitch(teleportSoundPitch);
	}

	private static float clampVolume(float volume) {
		return Math.clamp(volume, 0.0F, MAX_SOUND_VOLUME);
	}

	/** The sound packet only carries pitches in this range. */
	private static float clampPitch(float pitch) {
		return Math.clamp(pitch, MIN_SOUND_PITCH, MAX_SOUND_PITCH);
	}
}
