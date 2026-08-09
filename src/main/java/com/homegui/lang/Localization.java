package com.homegui.lang;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homegui.HomeGui;
import com.homegui.util.ColorCodes;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Server side translation of the mod's messages.
 *
 * <p>Minecraft already translates {@code Component.translatable} on the receiving client, but
 * that only works for players who have the mod installed. The server therefore reads the same
 * language files itself and resolves each message with the language the client reported in its
 * settings packet, so vanilla players see localised text too.
 */
public final class Localization {
	/** Language used when the client reports something the mod does not ship. */
	public static final String DEFAULT_LANGUAGE = "en_us";

	private static final String LANG_DIRECTORY = "assets/" + HomeGui.MOD_ID + "/lang";
	private static final String FILE_EXTENSION = ".json";
	private static final Gson GSON = new Gson();
	private static final Type TABLE_TYPE = new TypeToken<Map<String, String>>() {
	}.getType();

	/** Language code (lower case, e.g. {@code vi_vn}) to key/value table. */
	private static final Map<String, Map<String, String>> TABLES = new HashMap<>();

	private Localization() {
	}

	// ------------------------------------------------------------------ setup

	/** Reads every language file shipped inside the mod jar. Safe to call again on reload. */
	public static void load() {
		TABLES.clear();

		Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(HomeGui.MOD_ID);

		if (container.isPresent()) {
			Optional<Path> directory = container.get().findPath(LANG_DIRECTORY);

			if (directory.isPresent()) {
				readDirectory(directory.get());
			}
		}

		if (!TABLES.containsKey(DEFAULT_LANGUAGE)) {
			readDefaultFromClasspath();
		}

		HomeGui.LOGGER.info("Loaded {} language(s): {}", TABLES.size(), TABLES.keySet());
	}

	private static void readDirectory(Path directory) {
		try (Stream<Path> files = Files.list(directory)) {
			files.filter(path -> path.getFileName().toString().endsWith(FILE_EXTENSION))
					.forEach(Localization::readFile);
		} catch (Exception e) {
			HomeGui.LOGGER.error("Could not list the language directory", e);
		}
	}

	private static void readFile(Path path) {
		String fileName = path.getFileName().toString();
		String language = fileName.substring(0, fileName.length() - FILE_EXTENSION.length())
				.toLowerCase(Locale.ROOT);

		try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			Map<String, String> table = GSON.fromJson(reader, TABLE_TYPE);

			if (table != null) {
				TABLES.put(language, table);
			}
		} catch (Exception e) {
			HomeGui.LOGGER.error("Could not read language file {}", fileName, e);
		}
	}

	private static void readDefaultFromClasspath() {
		String resource = "/" + LANG_DIRECTORY + "/" + DEFAULT_LANGUAGE + FILE_EXTENSION;

		try (InputStream stream = Localization.class.getResourceAsStream(resource)) {
			if (stream == null) {
				HomeGui.LOGGER.error("Default language file {} is missing from the jar", resource);
				return;
			}

			Map<String, String> table = GSON.fromJson(
					new InputStreamReader(stream, StandardCharsets.UTF_8), TABLE_TYPE);

			if (table != null) {
				TABLES.put(DEFAULT_LANGUAGE, table);
			}
		} catch (Exception e) {
			HomeGui.LOGGER.error("Could not read the default language file", e);
		}
	}

	// ---------------------------------------------------------------- lookup

	/** Translates a message into the language the player's client is set to. */
	public static Component message(ServerPlayer player, String key, Object... args) {
		return message(languageOf(player), key, args);
	}

	/** The same, with the mod's chat prefix in front. Used for everything sent to chat. */
	public static Component prefixed(ServerPlayer player, String key, Object... args) {
		return prefixed(languageOf(player), key, args);
	}

	public static Component prefixed(CommandSourceStack source, String key, Object... args) {
		ServerPlayer player = source.getPlayer();
		return prefixed(player != null ? languageOf(player) : DEFAULT_LANGUAGE, key, args);
	}

	private static Component prefixed(String language, String key, Object... args) {
		return Component.empty()
				.append(message(language, Lang.PREFIX))
				.append(message(language, key, args));
	}

	/**
	 * Translates a message for a command source. Console and command blocks have no client
	 * language, so they fall back to {@link #DEFAULT_LANGUAGE}.
	 */
	public static Component message(CommandSourceStack source, String key, Object... args) {
		ServerPlayer player = source.getPlayer();
		return message(player != null ? languageOf(player) : DEFAULT_LANGUAGE, key, args);
	}

	public static Component message(String language, String key, Object... args) {
		return format(lookup(language, key), args);
	}

	/** The language code reported by the player's client, for example {@code vi_vn}. */
	public static String languageOf(ServerPlayer player) {
		String language = player.clientInformation().language();
		return language == null ? DEFAULT_LANGUAGE : language.toLowerCase(Locale.ROOT);
	}

	private static String lookup(String language, String key) {
		Map<String, String> table = tableFor(language);
		String value = table.get(key);

		if (value != null) {
			return value;
		}

		// A translation may be missing from a partially updated language file.
		value = defaultTable().get(key);
		return value != null ? value : key;
	}

	private static Map<String, String> tableFor(String language) {
		Map<String, String> exact = TABLES.get(language);

		if (exact != null) {
			return exact;
		}

		// en_gb, en_ca and friends should still get English rather than falling all the way back.
		int separator = language.indexOf('_');
		String prefix = (separator > 0 ? language.substring(0, separator) : language) + "_";

		for (Map.Entry<String, Map<String, String>> entry : TABLES.entrySet()) {
			if (entry.getKey().startsWith(prefix)) {
				return entry.getValue();
			}
		}

		return defaultTable();
	}

	private static Map<String, String> defaultTable() {
		return TABLES.getOrDefault(DEFAULT_LANGUAGE, Map.of());
	}

	/**
	 * Fills the {@code %s} placeholders in a template.
	 *
	 * <p>This is done by hand rather than with {@code String.format} because an argument may be
	 * a styled {@link Component}, such as a home name that carries colour codes. Each piece of
	 * the template becomes its own child component, so the colour that was active at the end of
	 * one piece is carried into the next one; without that, a {@code §7} at the start of a line
	 * would stop applying at the first placeholder.
	 */
	private static Component format(String template, Object... args) {
		if (args.length == 0) {
			return ColorCodes.parse(template);
		}

		MutableComponent result = Component.empty();
		StringBuilder pending = new StringBuilder();
		Style style = Style.EMPTY;
		int argIndex = 0;

		for (int index = 0; index < template.length(); index++) {
			char current = template.charAt(index);

			if (current == '%' && index + 1 < template.length()) {
				char next = template.charAt(index + 1);

				if (next == 's' && argIndex < args.length) {
					style = flush(result, pending, style);
					result.append(argument(args[argIndex++], style));
					index++;
					continue;
				}

				if (next == '%') {
					pending.append('%');
					index++;
					continue;
				}
			}

			pending.append(current);
		}

		flush(result, pending, style);
		return result;
	}

	/** Appends the pending text and returns the style left in effect after it. */
	private static Style flush(MutableComponent target, StringBuilder pending, Style style) {
		if (pending.isEmpty()) {
			return style;
		}

		String text = pending.toString();
		pending.setLength(0);
		target.append(ColorCodes.parse(text, style));
		return ColorCodes.styleAfter(style, text);
	}

	private static Component argument(Object argument, Style style) {
		if (argument instanceof Component component) {
			return component;
		}

		return Component.literal(String.valueOf(argument)).withStyle(style);
	}
}
