package com.homegui.client;

import com.homegui.config.HomeGuiConfig;
import com.homegui.lang.Lang;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * In game editor for {@code config/homegui.json}, reached through Mod Menu.
 *
 * <p>It writes the local config file, which is what single player uses. When connected to a
 * dedicated server the server's own copy is what counts, so the screen says as much.
 *
 * <p>Options are laid out in two columns and paged, so adding more of them later does not push
 * the screen past the bottom of the window.
 */
public class ConfigScreen extends Screen {
	private static final int COLUMNS = 2;
	private static final int ROWS_PER_COLUMN = 7;
	private static final int COLUMN_WIDTH = 224;
	private static final int LABEL_WIDTH = 118;
	private static final int VALUE_WIDTH = 98;
	private static final int ROW_HEIGHT = 24;
	private static final int WIDGET_HEIGHT = 20;
	private static final int BUTTON_WIDTH = 100;
	private static final int PAGE_BUTTON_WIDTH = 24;
	private static final int BUTTON_GAP = 8;
	private static final int TITLE_HEIGHT = 36;
	private static final int FOOTER_HEIGHT = 56;
	private static final int TEXT_BASELINE_OFFSET = 6;

	private static final int COLOR_TITLE = 0xFFFFFFFF;
	private static final int COLOR_LABEL = 0xFFC8C8C8;
	private static final int COLOR_NOTE = 0xFFA0A0A0;

	private final Screen parent;
	private final List<Option> options = new ArrayList<>();

	private int page;
	private int contentLeft;
	private int contentTop;
	private int noteY;

	public ConfigScreen(Screen parent) {
		super(Component.translatable(Lang.CONFIG_TITLE));
		this.parent = parent;
		buildOptions();
	}

	/** Reads the current config into editable rows. Order here is the order on screen. */
	private void buildOptions() {
		HomeGuiConfig config = HomeGuiConfig.get();

		options.add(new IntegerOption(Lang.CONFIG_MAX_HOMES, config.maxHomes,
				value -> config.maxHomes = value));
		options.add(new TextOption(Lang.CONFIG_DEFAULT_HOME_NAME, config.defaultHomeName,
				value -> config.defaultHomeName = value));
		options.add(new IntegerOption(Lang.CONFIG_TELEPORT_COOLDOWN, config.teleportCooldownSeconds,
				value -> config.teleportCooldownSeconds = value));
		options.add(new IntegerOption(Lang.CONFIG_TELEPORT_WARMUP, config.teleportWarmupSeconds,
				value -> config.teleportWarmupSeconds = value));
		options.add(new BooleanOption(Lang.CONFIG_CANCEL_WARMUP_ON_MOVE, config.cancelWarmupOnMove,
				value -> config.cancelWarmupOnMove = value));
		options.add(new DecimalOption(Lang.CONFIG_WARMUP_MOVE_TOLERANCE, config.warmupMoveTolerance,
				value -> config.warmupMoveTolerance = value));
		options.add(new BooleanOption(Lang.CONFIG_ALLOW_CROSS_DIMENSION, config.allowCrossDimension,
				value -> config.allowCrossDimension = value));
		options.add(new BooleanOption(Lang.CONFIG_ALLOW_OVERWRITE, config.allowOverwrite,
				value -> config.allowOverwrite = value));
		options.add(new BooleanOption(Lang.CONFIG_OP_BYPASS_LIMITS, config.opBypassLimits,
				value -> config.opBypassLimits = value));
		options.add(new IntegerOption(Lang.CONFIG_OP_PERMISSION_LEVEL, config.opPermissionLevel,
				value -> config.opPermissionLevel = value));
		options.add(new IntegerOption(Lang.CONFIG_MAX_HOME_NAME_LENGTH, config.maxHomeNameLength,
				value -> config.maxHomeNameLength = value));
		options.add(new IntegerOption(Lang.CONFIG_GUI_ENTRIES_PER_PAGE, config.guiEntriesPerPage,
				value -> config.guiEntriesPerPage = value));
		options.add(new BooleanOption(Lang.CONFIG_OPEN_GUI_ON_BARE_HOME, config.openGuiOnBareHomeCommand,
				value -> config.openGuiOnBareHomeCommand = value));

		options.add(new TextOption(Lang.CONFIG_WARMUP_TICK_SOUND, config.warmupTickSound,
				value -> config.warmupTickSound = value, true));
		options.add(new FloatOption(Lang.CONFIG_WARMUP_TICK_SOUND_VOLUME, config.warmupTickSoundVolume,
				value -> config.warmupTickSoundVolume = value));
		options.add(new FloatOption(Lang.CONFIG_WARMUP_TICK_SOUND_PITCH, config.warmupTickSoundPitch,
				value -> config.warmupTickSoundPitch = value));
		options.add(new TextOption(Lang.CONFIG_TELEPORT_SOUND, config.teleportSound,
				value -> config.teleportSound = value, true));
		options.add(new FloatOption(Lang.CONFIG_TELEPORT_SOUND_VOLUME, config.teleportSoundVolume,
				value -> config.teleportSoundVolume = value));
		options.add(new FloatOption(Lang.CONFIG_TELEPORT_SOUND_PITCH, config.teleportSoundPitch,
				value -> config.teleportSoundPitch = value));
	}

	// ------------------------------------------------------------------ init

	private int pageSize() {
		return COLUMNS * ROWS_PER_COLUMN;
	}

	private int pageCount() {
		return Math.max(1, (options.size() + pageSize() - 1) / pageSize());
	}

	@Override
	protected void init() {
		int contentWidth = COLUMN_WIDTH * COLUMNS;
		int contentHeight = TITLE_HEIGHT + ROWS_PER_COLUMN * ROW_HEIGHT + FOOTER_HEIGHT;

		contentLeft = (this.width - contentWidth) / 2;
		contentTop = Math.max(0, (this.height - contentHeight) / 2) + TITLE_HEIGHT;

		int first = page * pageSize();
		int last = Math.min(first + pageSize(), options.size());

		for (int index = first; index < last; index++) {
			int slot = index - first;
			int x = contentLeft + (slot / ROWS_PER_COLUMN) * COLUMN_WIDTH;
			int y = contentTop + (slot % ROWS_PER_COLUMN) * ROW_HEIGHT;

			this.addRenderableWidget(options.get(index).createWidget(
					this.font, x + LABEL_WIDTH, y, VALUE_WIDTH, WIDGET_HEIGHT));
		}

		int footerTop = contentTop + ROWS_PER_COLUMN * ROW_HEIGHT;
		noteY = footerTop + TEXT_BASELINE_OFFSET;

		buildFooter(footerTop + ROW_HEIGHT);
	}

	private void buildFooter(int y) {
		int width = BUTTON_WIDTH * 2 + BUTTON_GAP;
		boolean paged = pageCount() > 1;

		if (paged) {
			width += (PAGE_BUTTON_WIDTH + BUTTON_GAP) * 2;
		}

		int x = (this.width - width) / 2;

		if (paged) {
			this.addRenderableWidget(Button.builder(Component.translatable(Lang.GUI_PREVIOUS_PAGE), button -> {
				page = Math.max(0, page - 1);
				rebuildWidgets();
			}).bounds(x, y, PAGE_BUTTON_WIDTH, WIDGET_HEIGHT).build());

			x += PAGE_BUTTON_WIDTH + BUTTON_GAP;
		}

		this.addRenderableWidget(Button.builder(Component.translatable(Lang.CONFIG_SAVE), button -> save())
				.bounds(x, y, BUTTON_WIDTH, WIDGET_HEIGHT)
				.build());

		x += BUTTON_WIDTH + BUTTON_GAP;

		this.addRenderableWidget(Button.builder(Component.translatable(Lang.CONFIG_CANCEL), button -> onClose())
				.bounds(x, y, BUTTON_WIDTH, WIDGET_HEIGHT)
				.build());

		if (paged) {
			x += BUTTON_WIDTH + BUTTON_GAP;

			this.addRenderableWidget(Button.builder(Component.translatable(Lang.GUI_NEXT_PAGE), button -> {
				page = Math.min(pageCount() - 1, page + 1);
				rebuildWidgets();
			}).bounds(x, y, PAGE_BUTTON_WIDTH, WIDGET_HEIGHT).build());
		}
	}

	private void save() {
		for (Option option : options) {
			option.apply();
		}

		HomeGuiConfig.save();
		// Reloading runs the config through its own clamping and writes the tidied file back.
		HomeGuiConfig.load();
		onClose();
	}

	@Override
	public void onClose() {
		if (this.minecraft != null) {
			this.minecraft.gui.setScreen(parent);
		}
	}

	// ------------------------------------------------------------- rendering

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);

		int centerX = this.width / 2;

		graphics.centeredText(this.font, Component.translatable(Lang.CONFIG_TITLE),
				centerX, contentTop - TITLE_HEIGHT / 2, COLOR_TITLE);

		int first = page * pageSize();
		int last = Math.min(first + pageSize(), options.size());

		for (int index = first; index < last; index++) {
			int slot = index - first;
			int x = contentLeft + (slot / ROWS_PER_COLUMN) * COLUMN_WIDTH;
			int y = contentTop + (slot % ROWS_PER_COLUMN) * ROW_HEIGHT + TEXT_BASELINE_OFFSET;

			graphics.text(this.font, Component.translatable(options.get(index).labelKey()),
					x, y, COLOR_LABEL, false);
		}

		graphics.centeredText(this.font, Component.translatable(Lang.CONFIG_NOTE),
				centerX, noteY, COLOR_NOTE);

		if (pageCount() > 1) {
			graphics.centeredText(this.font,
					Component.translatable(Lang.CONFIG_PAGE, page + 1, pageCount()),
					centerX, contentTop - TITLE_HEIGHT / 2 + TEXT_BASELINE_OFFSET * 2, COLOR_NOTE);
		}
	}

	// --------------------------------------------------------------- options

	/** One editable config field, holding its pending value until Save is pressed. */
	private abstract static class Option {
		private final String labelKey;

		Option(String labelKey) {
			this.labelKey = labelKey;
		}

		String labelKey() {
			return labelKey;
		}

		abstract AbstractWidget createWidget(Font font, int x, int y, int width, int height);

		abstract void apply();
	}

	private static final class BooleanOption extends Option {
		private final Consumer<Boolean> setter;
		private boolean value;

		BooleanOption(String labelKey, boolean value, Consumer<Boolean> setter) {
			super(labelKey);
			this.value = value;
			this.setter = setter;
		}

		@Override
		AbstractWidget createWidget(Font font, int x, int y, int width, int height) {
			return Button.builder(label(), pressed -> {
				value = !value;
				pressed.setMessage(label());
			}).bounds(x, y, width, height).build();
		}

		private Component label() {
			return Component.translatable(value ? Lang.CONFIG_ENABLED : Lang.CONFIG_DISABLED);
		}

		@Override
		void apply() {
			setter.accept(value);
		}
	}

	/** Base for the fields typed as free text. */
	private abstract static class FieldOption extends Option {
		String text;

		FieldOption(String labelKey, String text) {
			super(labelKey);
			this.text = text;
		}

		@Override
		AbstractWidget createWidget(Font font, int x, int y, int width, int height) {
			EditBox box = new EditBox(font, x, y, width, height, Component.translatable(labelKey()));
			box.setMaxLength(MAX_FIELD_LENGTH);
			box.setValue(text);
			box.setResponder(value -> text = value);
			return box;
		}
	}

	private static final int MAX_FIELD_LENGTH = 64;

	private static final class TextOption extends FieldOption {
		private final Consumer<String> setter;
		/** Sound ids may legitimately be cleared, which is how the config turns a sound off. */
		private final boolean allowEmpty;

		TextOption(String labelKey, String value, Consumer<String> setter) {
			this(labelKey, value, setter, false);
		}

		TextOption(String labelKey, String value, Consumer<String> setter, boolean allowEmpty) {
			super(labelKey, value == null ? "" : value);
			this.setter = setter;
			this.allowEmpty = allowEmpty;
		}

		@Override
		void apply() {
			String trimmed = text.trim();

			if (allowEmpty || !trimmed.isEmpty()) {
				setter.accept(trimmed);
			}
		}
	}

	private static final class IntegerOption extends FieldOption {
		private final Consumer<Integer> setter;

		IntegerOption(String labelKey, int value, Consumer<Integer> setter) {
			super(labelKey, Integer.toString(value));
			this.setter = setter;
		}

		@Override
		void apply() {
			try {
				setter.accept(Integer.parseInt(text.trim()));
			} catch (NumberFormatException e) {
				// Typo in the box, keep whatever the config already had.
			}
		}
	}

	private static final class DecimalOption extends FieldOption {
		private final Consumer<Double> setter;

		DecimalOption(String labelKey, double value, Consumer<Double> setter) {
			super(labelKey, String.format(Locale.ROOT, "%.2f", value));
			this.setter = setter;
		}

		@Override
		void apply() {
			try {
				setter.accept(Double.parseDouble(text.trim()));
			} catch (NumberFormatException e) {
				// Typo in the box, keep whatever the config already had.
			}
		}
	}

	private static final class FloatOption extends FieldOption {
		private final Consumer<Float> setter;

		FloatOption(String labelKey, float value, Consumer<Float> setter) {
			super(labelKey, String.format(Locale.ROOT, "%.2f", value));
			this.setter = setter;
		}

		@Override
		void apply() {
			try {
				setter.accept(Float.parseFloat(text.trim()));
			} catch (NumberFormatException e) {
				// Typo in the box, keep whatever the config already had.
			}
		}
	}
}
