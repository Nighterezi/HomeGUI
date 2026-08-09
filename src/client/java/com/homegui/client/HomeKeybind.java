package com.homegui.client;

import com.homegui.lang.Lang;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * The key that opens the home screen, H by default.
 *
 * <p>There is deliberately no config option for this. The mapping shows up in Options, Controls
 * like any other key, so a player who does not want it clears the binding there, in the one
 * place they already look for keys.
 */
public final class HomeKeybind {
	private static KeyMapping openHomes;

	private HomeKeybind() {
	}

	public static void register() {
		openHomes = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				Lang.KEYBIND_OPEN,
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				KeyMapping.Category.MISC
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// consumeClick drains one press per call, so this has to run every tick rather than
			// only while a world is loaded, or presses would queue up behind a title screen.
			boolean pressed = openHomes.consumeClick();

			if (pressed && client.player != null) {
				HomeRequest.open();
			}
		});
	}
}
