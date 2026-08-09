package com.homegui.net;

import com.homegui.HomeService;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class HomeNetworking {
	private HomeNetworking() {
	}

	/** Registers the payload types. This has to run on both the client and the server. */
	public static void registerPayloads() {
		PayloadTypeRegistry.clientboundPlay().register(HomeListPayload.TYPE, HomeListPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(HomeActionPayload.TYPE, HomeActionPayload.CODEC);
	}

	/** Receives the actions a player triggers from the GUI. */
	public static void registerServerReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(HomeActionPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			MinecraftServer server = player.level().getServer();

			if (server == null) {
				return;
			}

			// Always handle the action on the server's main thread.
			server.execute(() -> handle(player, payload));
		});
	}

	private static void handle(ServerPlayer player, HomeActionPayload payload) {
		switch (payload.action()) {
			case HomeActionPayload.TELEPORT -> HomeService.teleportHome(player, payload.home());
			case HomeActionPayload.DELETE -> {
				HomeService.deleteHome(player, payload.home());
				HomeService.sendHomeList(player);
			}
			case HomeActionPayload.SET -> {
				HomeService.setHome(player, payload.home());
				HomeService.sendHomeList(player);
			}
			case HomeActionPayload.RENAME -> {
				HomeService.renameHome(player, payload.home(), payload.argument());
				HomeService.sendHomeList(player);
			}
			case HomeActionPayload.REFRESH -> HomeService.sendHomeList(player);
			default -> {
				// Unknown action, ignore it.
			}
		}
	}
}
