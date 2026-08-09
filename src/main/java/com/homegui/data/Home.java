package com.homegui.data;

import com.homegui.util.ColorCodes;

/**
 * A saved home. This is a plain class rather than a record so Gson can still read files
 * written by an older version that was missing some of the fields.
 */
public class Home {
	/** Display name, kept exactly as the player typed it. */
	public String name;
	/** Dimension id, for example {@code minecraft:overworld}. */
	public String dimension;
	public double x;
	public double y;
	public double z;
	public float yaw;
	public float pitch;
	public long createdAt;

	public Home() {
	}

	public Home(String name, String dimension, double x, double y, double z, float yaw, float pitch) {
		this.name = name;
		this.dimension = dimension;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.createdAt = System.currentTimeMillis();
	}

	/**
	 * Map key: the name without colour markup, lower cased. That way {@code /home House}
	 * finds {@code house}, and a home called {@code &ahouse} is still reachable by typing
	 * plain {@code house}.
	 */
	public String key() {
		return ColorCodes.key(name);
	}

	/** The name as it reads without any colour markup. */
	public String plainName() {
		return ColorCodes.strip(name);
	}
}
