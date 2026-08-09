package com.homegui.client;

import com.homegui.net.HomeActionPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Asks the server to open the home screen.
 *
 * <p>The client cannot open it on its own: the list of homes only exists server side, so both
 * the key mapping and the inventory button go through here and wait for the reply.
 */
public final class HomeRequest {
	private HomeRequest() {
	}

	/** False when the server does not have the mod, in which case there is nothing to ask. */
	public static boolean available() {
		return ClientPlayNetworking.canSend(HomeActionPayload.TYPE);
	}

	public static void open() {
		if (available()) {
			ClientPlayNetworking.send(HomeActionPayload.of(HomeActionPayload.OPEN, ""));
		}
	}
}
