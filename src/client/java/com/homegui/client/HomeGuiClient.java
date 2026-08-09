package com.homegui.client;

import com.homegui.net.HomeListPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Client entrypoint. It only listens for the home list the server sends and hands it to
 * {@link HomeScreen}; everything else happens on the server.
 */
public class HomeGuiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(HomeListPayload.TYPE, (payload, context) -> {
			HomeListData data = HomeListData.parse(payload.json());
			context.client().execute(() -> HomeScreen.openOrUpdate(data));
		});
	}
}
