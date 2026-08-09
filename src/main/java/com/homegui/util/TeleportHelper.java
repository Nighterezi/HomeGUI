package com.homegui.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Teleporting is isolated here so that an API change in a future Minecraft release
 * only has to be fixed in one place.
 */
public final class TeleportHelper {
	private TeleportHelper() {
	}

	public static void teleport(ServerPlayer player, ServerLevel level, double x, double y, double z, float yaw, float pitch) {
		player.teleport(new TeleportTransition(
				level,
				new Vec3(x, y, z),
				Vec3.ZERO,
				yaw,
				pitch,
				TeleportTransition.DO_NOTHING
		));
	}
}
