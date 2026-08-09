package com.homegui.client;

import com.homegui.net.HomeListPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Client entrypoint. It listens for the home list the server sends, and registers the two ways
 * of asking for it without a command. Every decision about homes still happens on the server.
 */
public class HomeGuiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(HomeListPayload.TYPE, (payload, context) -> {
			HomeListData data = HomeListData.parse(payload.json());
			context.client().execute(() -> HomeScreen.openOrUpdate(data));
		});

		HomeKeybind.register();
		InventoryButton.register();
	}
}
