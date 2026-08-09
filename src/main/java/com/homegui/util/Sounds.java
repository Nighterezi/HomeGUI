package com.homegui.util;

import com.homegui.HomeGui;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Plays a configured sound to a single player.
 *
 * <p>The packet is sent straight down the player's connection instead of going through the
 * level, so nobody standing nearby hears someone else's teleport countdown.
 */
public final class Sounds {
	/** Ids already reported as unusable, so a bad config line is not logged every second. */
	private static final Set<String> REPORTED = ConcurrentHashMap.newKeySet();

	private Sounds() {
	}

	/** Does nothing when the id is empty, which is how the config disables a sound. */
	public static void playTo(ServerPlayer player, String soundId, float volume, float pitch) {
		if (soundId == null || soundId.isBlank()) {
			return;
		}

		Identifier id = Identifier.tryParse(soundId);
		Optional<Holder.Reference<SoundEvent>> sound = id == null
				? Optional.empty()
				: BuiltInRegistries.SOUND_EVENT.get(id);

		if (sound.isEmpty()) {
			if (REPORTED.add(soundId)) {
				HomeGui.LOGGER.warn("Unknown sound id in config: {}", soundId);
			}

			return;
		}

		player.connection.send(new ClientboundSoundPacket(
				sound.get(),
				SoundSource.PLAYERS,
				player.getX(), player.getY(), player.getZ(),
				volume, pitch,
				player.getRandom().nextLong()
		));
	}

	/** Called when the config is reloaded so a fixed id is reported again if it is still wrong. */
	public static void forgetWarnings() {
		REPORTED.clear();
	}
}
