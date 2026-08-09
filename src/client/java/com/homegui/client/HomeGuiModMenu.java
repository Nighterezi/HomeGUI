package com.homegui.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Adds the Config button next to HomeGUI in the Mods list.
 *
 * <p>Only Mod Menu ever loads this entrypoint, so the mod runs perfectly well on installs
 * that do not have Mod Menu.
 */
public class HomeGuiModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ConfigScreen::new;
	}
}
