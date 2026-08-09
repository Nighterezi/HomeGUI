package com.homegui.net;

import com.homegui.HomeGui;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Server to client. Carries the whole home list as JSON so the client can build the screen.
 * A single JSON string keeps the packet easy to extend without touching the codec.
 */
public record HomeListPayload(String json) implements CustomPacketPayload {
	public static final Identifier ID = HomeGui.id("home_list");
	public static final CustomPacketPayload.Type<HomeListPayload> TYPE = new CustomPacketPayload.Type<>(ID);

	public static final StreamCodec<RegistryFriendlyByteBuf, HomeListPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.stringUtf8(1_048_576), HomeListPayload::json,
			HomeListPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
