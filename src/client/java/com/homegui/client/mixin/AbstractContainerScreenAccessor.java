package com.homegui.client.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Reads where the inventory panel ended up on screen.
 *
 * <p>These fields are protected and there is no getter, but the button HomeGUI adds has to sit
 * against the edge of the panel, which moves when the recipe book opens.
 */
@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
	@Accessor("leftPos")
	int homegui$leftPos();

	@Accessor("topPos")
	int homegui$topPos();

	@Accessor("imageWidth")
	int homegui$imageWidth();
}
