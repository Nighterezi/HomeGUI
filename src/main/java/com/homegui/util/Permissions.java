package com.homegui.util;

import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionSet;

/**
 * Minecraft 26.2 replaced numeric permission levels with {@link PermissionCheck}.
 * This maps the number kept in the config onto the matching check.
 */
public final class Permissions {
	private Permissions() {
	}

	public static PermissionCheck byLevel(int level) {
		return switch (level) {
			case 0 -> Commands.LEVEL_ALL;
			case 1 -> Commands.LEVEL_MODERATORS;
			case 3 -> Commands.LEVEL_ADMINS;
			case 4 -> Commands.LEVEL_OWNERS;
			default -> Commands.LEVEL_GAMEMASTERS;
		};
	}

	public static boolean has(PermissionSet permissions, int level) {
		return byLevel(level).check(permissions);
	}
}
