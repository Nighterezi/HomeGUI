package com.homegui.net;

import com.homegui.HomeGui;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Client to server. One action the player triggered from the GUI.
 *
 * @param action   one of {@link #TELEPORT}, {@link #DELETE}, {@link #SET},
 *                 {@link #RENAME} or {@link #REFRESH}
 * @param home     the home the action applies to, empty for {@link #REFRESH}
 * @param argument the new name for {@link #RENAME}, empty otherwise
 */
public record HomeActionPayload(String action, String home, String argument) implements CustomPacketPayload {
	public static final String TELEPORT = "teleport";
	public static final String DELETE = "delete";
	public static final String SET = "set";
	public static final String RENAME = "rename";
	public static final String REFRESH = "refresh";

	/** Generous enough for any name the server would accept, small enough to bound the packet. */
	private static final int MAX_NAME_BYTES = 256;
	private static final int MAX_ACTION_BYTES = 32;

	public static final Identifier ID = HomeGui.id("home_action");
	public static final CustomPacketPayload.Type<HomeActionPayload> TYPE = new CustomPacketPayload.Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, HomeActionPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.stringUtf8(MAX_ACTION_BYTES), HomeActionPayload::action,
			ByteBufCodecs.stringUtf8(MAX_NAME_BYTES), HomeActionPayload::home,
			ByteBufCodecs.stringUtf8(MAX_NAME_BYTES), HomeActionPayload::argument,
			HomeActionPayload::new
	);

	public static HomeActionPayload of(String action, String home) {
		return new HomeActionPayload(action, home, "");
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
