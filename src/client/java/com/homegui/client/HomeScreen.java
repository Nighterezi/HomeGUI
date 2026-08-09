package com.homegui.client;

import com.homegui.HomeGui;
import com.homegui.lang.Lang;
import com.homegui.net.HomeActionPayload;
import com.homegui.util.ColorCodes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Locale;

/**
 * The dialog that pops up on /home.
 *
 * <p>Each row is a home: clicking it teleports, {@code R} starts renaming it, and {@code X}
 * deletes it after a second click. The field at the bottom doubles as the name for a new home
 * and as the new name while renaming.
 *
 * <p>All of the text goes through {@link Component#translatable}, so the client renders it in
 * whichever language the player has selected.
 */
public class HomeScreen extends Screen {
	private static final int PANEL_WIDTH = 326;
	private static final int PANEL_PADDING = 6;
	private static final int ROW_HEIGHT = 24;
	private static final int WIDGET_HEIGHT = 20;
	private static final int WIDGET_GAP = 4;
	private static final int NAME_BUTTON_WIDTH = 258;
	private static final int ICON_BUTTON_WIDTH = 24;
	private static final int PAGE_BUTTON_WIDTH = 40;
	private static final int NAME_FIELD_WIDTH = 200;
	private static final int ACTION_BUTTON_WIDTH = 100;
	private static final int CLOSE_BUTTON_WIDTH = 100;
	private static final int TITLE_HEIGHT = 34;
	private static final int ROW_GAP = 26;
	private static final int TITLE_OFFSET = 10;
	private static final int HINT_GAP = 5;
	private static final int HINT_ROW_HEIGHT = 20;

	/** Pencil icons stitched into the GUI atlas from assets/homegui/textures/gui/sprites. */
	private static final Identifier PENCIL_SPRITE = HomeGui.id("icon/pencil");
	private static final Identifier PENCIL_ACTIVE_SPRITE = HomeGui.id("icon/pencil_active");
	private static final int ICON_SIZE = 16;

	private static final int COLOR_PANEL = 0xB0101018;
	private static final int COLOR_TITLE = 0xFFFFFFFF;
	private static final int COLOR_HINT = 0xFFA0A0A0;
	private static final int COLOR_WARNING = 0xFFFF6B6B;

	/** The screen that is currently open, so an update packet can find it without asking Minecraft. */
	private static HomeScreen opened;

	private HomeListData data;
	private int page;
	/** Home waiting for a second click on its delete button, or null. */
	private String pendingDelete;
	/** Home currently being renamed, or null when the field creates a new home instead. */
	private String renaming;
	/** Kept outside the widget so the text survives a rebuild. */
	private String inputText = "";

	private EditBox nameField;

	private int panelLeft;
	private int panelTop;
	private int panelHeight;
	private int listTop;
	private int listRows;
	private int hintY;

	public HomeScreen(HomeListData data) {
		super(Component.translatable(Lang.GUI_TITLE, data.homes().size(), data.maxHomes()));
		this.data = data;
	}

	/** Opens the screen, or refreshes it when it is already showing. */
	public static void openOrUpdate(HomeListData data) {
		HomeScreen current = opened;

		if (current != null) {
			current.update(data);
		} else if (data.open()) {
			Minecraft.getInstance().gui.setScreen(new HomeScreen(data));
		}
	}

	/** Applies a list the server pushed down while the screen was open. */
	public void update(HomeListData newData) {
		this.data = newData;
		this.pendingDelete = null;
		this.renaming = null;
		this.inputText = "";

		if (this.page >= newData.pageCount()) {
			this.page = newData.pageCount() - 1;
		}

		if (this.minecraft != null) {
			this.rebuildWidgets();
		}
	}

	// ------------------------------------------------------------------ init

	@Override
	protected void init() {
		opened = this;

		// A single page shrinks to the homes it actually holds; once there are several pages
		// the height stays put so paging does not make the dialog jump around.
		int rows = data.pageCount() > 1 ? data.perPage() : Math.max(1, data.homes().size());
		int listHeight = rows * ROW_HEIGHT;

		panelHeight = TITLE_HEIGHT + listHeight + WIDGET_GAP + ROW_GAP * 3 + HINT_ROW_HEIGHT;
		panelLeft = (this.width - PANEL_WIDTH) / 2;
		panelTop = Math.max(WIDGET_GAP, (this.height - panelHeight) / 2);
		listTop = panelTop + TITLE_HEIGHT;

		int paginationTop = listTop + listHeight + WIDGET_GAP;
		int footerTop = paginationTop + ROW_GAP;
		int closeTop = footerTop + ROW_GAP;

		// The hint sits under the Close button rather than over it.
		hintY = closeTop + WIDGET_HEIGHT + HINT_GAP;
		listRows = rows;

		buildHomeRows();
		buildPagination(paginationTop);
		buildFooter(footerTop, closeTop);
	}

	private void buildHomeRows() {
		List<HomeListData.Entry> entries = data.page(page);

		int renameX = panelLeft + PANEL_PADDING + NAME_BUTTON_WIDTH + WIDGET_GAP;
		int deleteX = renameX + ICON_BUTTON_WIDTH + WIDGET_GAP;

		for (int index = 0; index < entries.size(); index++) {
			HomeListData.Entry entry = entries.get(index);
			String home = entry.name();
			int y = listTop + index * ROW_HEIGHT;

			// The button shows only the name; the position lives in the tooltip so a long
			// name never has to compete with the coordinates for room.
			this.addRenderableWidget(Button.builder(labelFor(entry), button -> teleport(home))
					.bounds(panelLeft + PANEL_PADDING, y, NAME_BUTTON_WIDTH, WIDGET_HEIGHT)
					.tooltip(Tooltip.create(tooltipFor(entry)))
					.build());

			boolean editing = home.equals(renaming);

			SpriteIconButton rename = SpriteIconButton
					.builder(Component.translatable(Lang.GUI_RENAME_TOOLTIP),
							button -> onRenameClicked(home), true)
					.sprite(editing ? PENCIL_ACTIVE_SPRITE : PENCIL_SPRITE, ICON_SIZE, ICON_SIZE)
					.size(ICON_BUTTON_WIDTH, WIDGET_HEIGHT)
					.build();
			rename.setPosition(renameX, y);
			rename.setTooltip(Tooltip.create(Component.translatable(Lang.GUI_RENAME_TOOLTIP)));
			this.addRenderableWidget(rename);

			// Red once the delete is armed. The colour lives here rather than in the language
			// file so translators only ever deal with wording.
			Component deleteLabel = Component.translatable(Lang.GUI_DELETE)
					.withStyle(home.equals(pendingDelete) ? ChatFormatting.RED : ChatFormatting.WHITE);

			this.addRenderableWidget(Button.builder(deleteLabel, button -> onDeleteClicked(home))
					.bounds(deleteX, y, ICON_BUTTON_WIDTH, WIDGET_HEIGHT)
					.tooltip(Tooltip.create(Component.translatable(Lang.GUI_DELETE_TOOLTIP)))
					.build());
		}
	}

	private void buildPagination(int y) {
		if (data.pageCount() <= 1) {
			return;
		}

		this.addRenderableWidget(Button.builder(Component.translatable(Lang.GUI_PREVIOUS_PAGE), button -> {
			page = Math.max(0, page - 1);
			clearPendingActions();
			rebuildWidgets();
		}).bounds(panelLeft + PANEL_PADDING, y, PAGE_BUTTON_WIDTH, WIDGET_HEIGHT).build());

		this.addRenderableWidget(Button.builder(Component.translatable(Lang.GUI_NEXT_PAGE), button -> {
			page = Math.min(data.pageCount() - 1, page + 1);
			clearPendingActions();
			rebuildWidgets();
		}).bounds(panelLeft + PANEL_WIDTH - PANEL_PADDING - PAGE_BUTTON_WIDTH, y,
				PAGE_BUTTON_WIDTH, WIDGET_HEIGHT).build());
	}

	private void buildFooter(int y, int closeTop) {
		nameField = new EditBox(this.font, panelLeft + PANEL_PADDING, y, NAME_FIELD_WIDTH, WIDGET_HEIGHT,
				Component.translatable(Lang.GUI_NAME_FIELD));
		nameField.setMaxLength(data.maxNameLength());
		nameField.setHint(Component.translatable(Lang.GUI_NAME_FIELD));
		nameField.setValue(inputText);
		nameField.setResponder(value -> inputText = value);
		this.addRenderableWidget(nameField);

		Component actionLabel = Component.translatable(
				renaming != null ? Lang.GUI_RENAME_CONFIRM : Lang.GUI_SET_HOME);

		this.addRenderableWidget(Button.builder(actionLabel, button -> submit())
				.bounds(panelLeft + PANEL_WIDTH - PANEL_PADDING - ACTION_BUTTON_WIDTH, y,
						ACTION_BUTTON_WIDTH, WIDGET_HEIGHT)
				.build());

		this.addRenderableWidget(Button.builder(Component.translatable(Lang.GUI_CLOSE), button -> onClose())
				.bounds(panelLeft + (PANEL_WIDTH - CLOSE_BUTTON_WIDTH) / 2, closeTop,
						CLOSE_BUTTON_WIDTH, WIDGET_HEIGHT)
				.build());
	}

	// --------------------------------------------------------------- actions

	private void teleport(String name) {
		ClientPlayNetworking.send(HomeActionPayload.of(HomeActionPayload.TELEPORT, name));
		onClose();
	}

	/** First click arms the delete, a second click on the same home confirms it. */
	private void onDeleteClicked(String name) {
		if (name.equals(pendingDelete)) {
			ClientPlayNetworking.send(HomeActionPayload.of(HomeActionPayload.DELETE, name));
			pendingDelete = null;
		} else {
			pendingDelete = name;
			renaming = null;
		}

		rebuildWidgets();
	}

	/** Clicking rename loads the current name into the field; clicking it again cancels. */
	private void onRenameClicked(String name) {
		if (name.equals(renaming)) {
			renaming = null;
			inputText = "";
		} else {
			renaming = name;
			pendingDelete = null;
			inputText = name;
		}

		rebuildWidgets();
	}

	/** The footer button either renames the selected home or creates a new one. */
	private void submit() {
		String value = inputText.trim();

		if (renaming != null) {
			if (value.isEmpty()) {
				// An empty box would fall back to the default name, which is never what a
				// rename was meant to do.
				return;
			}

			ClientPlayNetworking.send(new HomeActionPayload(HomeActionPayload.RENAME, renaming, value));
		} else {
			ClientPlayNetworking.send(HomeActionPayload.of(HomeActionPayload.SET, value));
		}

		// The server answers with a fresh list, which resets the field and the pending state.
	}

	private void clearPendingActions() {
		pendingDelete = null;
		renaming = null;
	}

	@Override
	public void onClose() {
		if (this.minecraft != null) {
			this.minecraft.gui.setScreen(null);
		}
	}

	@Override
	public void removed() {
		super.removed();

		if (opened == this) {
			opened = null;
		}
	}

	// ------------------------------------------------------------- rendering

	/** Draws the dialog backdrop. This runs before the widgets, unlike extractRenderState. */
	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractBackground(graphics, mouseX, mouseY, delta);

		graphics.fillGradient(panelLeft, panelTop, panelLeft + PANEL_WIDTH, panelTop + panelHeight,
				COLOR_PANEL, COLOR_PANEL);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics, mouseX, mouseY, delta);

		int centerX = panelLeft + PANEL_WIDTH / 2;

		graphics.centeredText(this.font,
				Component.translatable(Lang.GUI_TITLE, data.homes().size(), data.maxHomes()),
				centerX, panelTop + TITLE_OFFSET, COLOR_TITLE);

		if (data.homes().isEmpty()) {
			graphics.centeredText(this.font, Component.translatable(Lang.GUI_EMPTY),
					centerX, listTop + 8, COLOR_HINT);
		}

		if (data.pageCount() > 1) {
			graphics.centeredText(this.font,
					Component.translatable(Lang.GUI_PAGE, page + 1, data.pageCount()),
					centerX, listTop + listRows * ROW_HEIGHT + TITLE_OFFSET, COLOR_HINT);
		}

		if (renaming != null) {
			graphics.centeredText(this.font,
					Component.translatable(Lang.GUI_RENAME_HINT, ColorCodes.parse(renaming)),
					centerX, hintY, COLOR_HINT);
		} else if (pendingDelete != null) {
			graphics.centeredText(this.font,
					Component.translatable(Lang.GUI_DELETE_HINT, ColorCodes.parse(pendingDelete)),
					centerX, hintY, COLOR_WARNING);
		}
	}

	/** The button shows the name exactly as the player styled it. */
	private Component labelFor(HomeListData.Entry entry) {
		return ColorCodes.parse(entry.name());
	}

	/**
	 * The name keeps its own colours, while the position lines are greyed as a whole. Colouring
	 * them with codes inside the template would not work: a translation argument starts a new
	 * child component, so the code would stop applying at the first placeholder.
	 */
	private Component tooltipFor(HomeListData.Entry entry) {
		return Component.empty()
				.append(ColorCodes.parse(entry.name()))
				.append(CommonComponents.NEW_LINE)
				.append(Component.translatable(Lang.GUI_ENTRY_LOCATION,
								shortDimension(entry.dimension()),
								coordinate(entry.x()), coordinate(entry.y()), coordinate(entry.z()))
						.withStyle(ChatFormatting.GRAY));
	}

	/** {@code minecraft:the_nether} reads better as {@code the_nether} on a button. */
	private static String shortDimension(String dimensionId) {
		if (dimensionId == null || dimensionId.isEmpty()) {
			return "";
		}

		int separator = dimensionId.indexOf(':');
		return separator >= 0 ? dimensionId.substring(separator + 1) : dimensionId;
	}

	private static String coordinate(double value) {
		return String.format(Locale.ROOT, "%.0f", value);
	}
}
