package com.homegui.client;

import com.homegui.HomeGui;
import com.homegui.client.mixin.AbstractContainerScreenAccessor;
import com.homegui.config.HomeGuiConfig;
import com.homegui.lang.Lang;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Puts a small house button beside the survival inventory so the screen can be reached without
 * typing a command.
 */
public final class InventoryButton {
	private static final Identifier HOUSE_SPRITE = HomeGui.id("icon/house");
	private static final int ICON_SIZE = 16;
	private static final int BUTTON_SIZE = 20;
	/** Gap between the inventory panel and the button, so it clears the border. */
	private static final int PANEL_GAP = 2;
	private static final int TOP_INSET = 4;

	private InventoryButton() {
	}

	public static void register() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (screen instanceof InventoryScreen inventory) {
				addTo(inventory);
			}
		});
	}

	private static void addTo(Screen screen) {
		// Checked on every open rather than once, so toggling the option applies immediately.
		if (!HomeGuiConfig.get().showInventoryButton || !HomeRequest.available()) {
			return;
		}

		AbstractContainerScreenAccessor panel = (AbstractContainerScreenAccessor) screen;

		SpriteIconButton button = SpriteIconButton
				.builder(Component.translatable(Lang.GUI_OPEN_TOOLTIP), pressed -> HomeRequest.open(), true)
				.sprite(HOUSE_SPRITE, ICON_SIZE, ICON_SIZE)
				.size(BUTTON_SIZE, BUTTON_SIZE)
				.build();

		button.setPosition(
				panel.homegui$leftPos() + panel.homegui$imageWidth() + PANEL_GAP,
				panel.homegui$topPos() + TOP_INSET);
		button.setTooltip(Tooltip.create(Component.translatable(Lang.GUI_OPEN_TOOLTIP)));

		Screens.getWidgets(screen).add(button);
	}
}
