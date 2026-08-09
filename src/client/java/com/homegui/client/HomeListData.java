package com.homegui.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The home list the server sent, unpacked from JSON. Everything the screen needs to lay
 * itself out comes from here, so no server side option is duplicated on the client.
 */
public record HomeListData(
		boolean open,
		int maxHomes,
		int perPage,
		int maxNameLength,
		String currentDimension,
		List<Entry> homes
) {
	public record Entry(String name, String dimension, double x, double y, double z) {
	}

	private static final int FALLBACK_PER_PAGE = 6;
	private static final int FALLBACK_NAME_LENGTH = 24;

	public static HomeListData empty() {
		return new HomeListData(false, 0, FALLBACK_PER_PAGE, FALLBACK_NAME_LENGTH, "", List.of());
	}

	public static HomeListData parse(String json) {
		try {
			JsonObject root = JsonParser.parseString(json).getAsJsonObject();

			return new HomeListData(
					bool(root, "open"),
					integer(root, "maxHomes", 0),
					Math.max(1, integer(root, "perPage", FALLBACK_PER_PAGE)),
					Math.max(1, integer(root, "maxNameLength", FALLBACK_NAME_LENGTH)),
					string(root, "currentDimension"),
					entries(root)
			);
		} catch (Exception e) {
			return empty();
		}
	}

	private static List<Entry> entries(JsonObject root) {
		List<Entry> homes = new ArrayList<>();
		JsonElement array = root.get("homes");

		if (array == null || !array.isJsonArray()) {
			return homes;
		}

		for (JsonElement element : array.getAsJsonArray()) {
			JsonObject entry = element.getAsJsonObject();
			homes.add(new Entry(
					entry.get("name").getAsString(),
					entry.get("dimension").getAsString(),
					entry.get("x").getAsDouble(),
					entry.get("y").getAsDouble(),
					entry.get("z").getAsDouble()
			));
		}

		return homes;
	}

	private static boolean bool(JsonObject object, String key) {
		return object.has(key) && object.get(key).getAsBoolean();
	}

	private static int integer(JsonObject object, String key, int fallback) {
		return object.has(key) ? object.get(key).getAsInt() : fallback;
	}

	private static String string(JsonObject object, String key) {
		return object.has(key) ? object.get(key).getAsString() : "";
	}

	/** Number of pages, at least one even when there are no homes. */
	public int pageCount() {
		return Math.max(1, (homes.size() + perPage - 1) / perPage);
	}

	public List<Entry> page(int index) {
		int from = Math.min(index * perPage, homes.size());
		int to = Math.min(from + perPage, homes.size());
		return homes.subList(from, to);
	}
}
