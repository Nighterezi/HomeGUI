package com.homegui.lang;

/**
 * Every translation key used by the mod. Keeping them here means the Java code never
 * contains user facing text; the wording itself lives in {@code assets/homegui/lang/*.json}.
 */
public final class Lang {
	public static final String HOME_SET = "homegui.message.home_set";
	public static final String HOME_DELETED = "homegui.message.home_deleted";
	public static final String HOME_RENAMED = "homegui.message.home_renamed";
	public static final String HOME_TELEPORTED = "homegui.message.home_teleported";
	public static final String HOME_NOT_FOUND = "homegui.message.home_not_found";
	public static final String HOME_ALREADY_EXISTS = "homegui.message.home_already_exists";
	public static final String LIMIT_REACHED = "homegui.message.limit_reached";
	public static final String COOLDOWN = "homegui.message.cooldown";
	public static final String WARMUP_STARTED = "homegui.message.warmup_started";
	public static final String WARMUP_COUNTDOWN = "homegui.message.warmup_countdown";
	public static final String WARMUP_CANCELLED = "homegui.message.warmup_cancelled";
	public static final String CROSS_DIMENSION_DENIED = "homegui.message.cross_dimension_denied";
	public static final String INVALID_NAME = "homegui.message.invalid_name";
	public static final String NO_HOMES = "homegui.message.no_homes";
	public static final String LIST_HEADER = "homegui.message.list_header";
	public static final String LIST_ENTRY = "homegui.message.list_entry";
	public static final String CONFIG_RELOADED = "homegui.message.config_reloaded";
	public static final String DIMENSION_MISSING = "homegui.message.dimension_missing";

	public static final String GUI_TITLE = "homegui.gui.title";
	public static final String GUI_EMPTY = "homegui.gui.empty";
	public static final String GUI_PAGE = "homegui.gui.page";
	public static final String GUI_ENTRY = "homegui.gui.entry";
	public static final String GUI_ENTRY_TOOLTIP = "homegui.gui.entry_tooltip";
	public static final String GUI_RENAME_TOOLTIP = "homegui.gui.rename_tooltip";
	public static final String GUI_RENAME_HINT = "homegui.gui.rename_hint";
	public static final String GUI_RENAME_CONFIRM = "homegui.gui.rename_confirm";
	public static final String GUI_DELETE = "homegui.gui.delete";
	public static final String GUI_DELETE_CONFIRM = "homegui.gui.delete_confirm";
	public static final String GUI_DELETE_TOOLTIP = "homegui.gui.delete_tooltip";
	public static final String GUI_DELETE_HINT = "homegui.gui.delete_hint";
	public static final String GUI_NAME_FIELD = "homegui.gui.name_field";
	public static final String GUI_SET_HOME = "homegui.gui.set_home";
	public static final String GUI_CLOSE = "homegui.gui.close";
	public static final String GUI_PREVIOUS_PAGE = "homegui.gui.previous_page";
	public static final String GUI_NEXT_PAGE = "homegui.gui.next_page";

	public static final String CONFIG_TITLE = "homegui.config.title";
	public static final String CONFIG_NOTE = "homegui.config.note";
	public static final String CONFIG_PAGE = "homegui.config.page";
	public static final String CONFIG_SAVE = "homegui.config.save";
	public static final String CONFIG_CANCEL = "homegui.config.cancel";
	public static final String CONFIG_ENABLED = "homegui.config.enabled";
	public static final String CONFIG_DISABLED = "homegui.config.disabled";
	public static final String CONFIG_MAX_HOMES = "homegui.config.max_homes";
	public static final String CONFIG_DEFAULT_HOME_NAME = "homegui.config.default_home_name";
	public static final String CONFIG_TELEPORT_COOLDOWN = "homegui.config.teleport_cooldown_seconds";
	public static final String CONFIG_TELEPORT_WARMUP = "homegui.config.teleport_warmup_seconds";
	public static final String CONFIG_CANCEL_WARMUP_ON_MOVE = "homegui.config.cancel_warmup_on_move";
	public static final String CONFIG_WARMUP_MOVE_TOLERANCE = "homegui.config.warmup_move_tolerance";
	public static final String CONFIG_ALLOW_CROSS_DIMENSION = "homegui.config.allow_cross_dimension";
	public static final String CONFIG_ALLOW_OVERWRITE = "homegui.config.allow_overwrite";
	public static final String CONFIG_OP_BYPASS_LIMITS = "homegui.config.op_bypass_limits";
	public static final String CONFIG_OP_PERMISSION_LEVEL = "homegui.config.op_permission_level";
	public static final String CONFIG_MAX_HOME_NAME_LENGTH = "homegui.config.max_home_name_length";
	public static final String CONFIG_GUI_ENTRIES_PER_PAGE = "homegui.config.gui_entries_per_page";
	public static final String CONFIG_OPEN_GUI_ON_BARE_HOME = "homegui.config.open_gui_on_bare_home_command";
	public static final String CONFIG_WARMUP_TICK_SOUND = "homegui.config.warmup_tick_sound";
	public static final String CONFIG_WARMUP_TICK_SOUND_VOLUME = "homegui.config.warmup_tick_sound_volume";
	public static final String CONFIG_WARMUP_TICK_SOUND_PITCH = "homegui.config.warmup_tick_sound_pitch";
	public static final String CONFIG_TELEPORT_SOUND = "homegui.config.teleport_sound";
	public static final String CONFIG_TELEPORT_SOUND_VOLUME = "homegui.config.teleport_sound_volume";
	public static final String CONFIG_TELEPORT_SOUND_PITCH = "homegui.config.teleport_sound_pitch";

	private Lang() {
	}
}
