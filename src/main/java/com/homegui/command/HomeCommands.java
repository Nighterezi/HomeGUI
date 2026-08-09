package com.homegui.command;

import com.homegui.HomeService;
import com.homegui.config.HomeGuiConfig;
import com.homegui.data.Home;
import com.homegui.data.HomeManager;
import com.homegui.lang.Lang;
import com.homegui.lang.Localization;
import com.homegui.util.Permissions;
import com.homegui.util.Sounds;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.server.level.ServerPlayer;

/**
 * Brigadier registration for the mod's commands. Every branch delegates to
 * {@link HomeService} so the commands behave exactly like the GUI buttons.
 *
 * <p>Home names may contain spaces, so the name argument is greedy. That lets players type
 * {@code /home my summer house} without quoting.
 */
public final class HomeCommands {
	private static final String NAME_ARGUMENT = "name";

	private static final SuggestionProvider<CommandSourceStack> HOME_NAMES = (context, builder) -> {
		ServerPlayer player = context.getSource().getPlayer();

		if (player == null) {
			return builder.buildFuture();
		}

		return SharedSuggestionProvider.suggest(
				HomeManager.listOf(player.getUUID()).stream().map(home -> home.name),
				builder);
	};

	private HomeCommands() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		// /sethome [name]
		dispatcher.register(Commands.literal("sethome")
				.executes(context -> setHome(context, HomeGuiConfig.get().defaultHomeName))
				.then(Commands.argument(NAME_ARGUMENT, StringArgumentType.greedyString())
						.executes(context -> setHome(context, name(context)))));

		// /home [name]
		dispatcher.register(Commands.literal("home")
				.executes(HomeCommands::bareHome)
				.then(Commands.argument(NAME_ARGUMENT, StringArgumentType.greedyString())
						.suggests(HOME_NAMES)
						.executes(context -> teleport(context, name(context)))));

		// /delhome [name]
		dispatcher.register(Commands.literal("delhome")
				.executes(context -> deleteHome(context, HomeGuiConfig.get().defaultHomeName))
				.then(Commands.argument(NAME_ARGUMENT, StringArgumentType.greedyString())
						.suggests(HOME_NAMES)
						.executes(context -> deleteHome(context, name(context)))));

		// /homes, opens the GUI or prints the list for vanilla clients
		dispatcher.register(Commands.literal("homes")
				.executes(HomeCommands::openOrList));

		// /homegui reload
		dispatcher.register(Commands.literal("homegui")
				.requires(source -> Permissions.has(source.permissions(), HomeGuiConfig.get().opPermissionLevel))
				.then(Commands.literal("reload")
						.executes(HomeCommands::reload)));
	}

	// -------------------------------------------------------------- actions

	private static int setHome(CommandContext<CommandSourceStack> context, String name)
			throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		boolean changed = HomeService.setHome(player, name);
		HomeService.sendHomeList(player);
		return changed ? 1 : 0;
	}

	private static int deleteHome(CommandContext<CommandSourceStack> context, String name)
			throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		boolean changed = HomeService.deleteHome(player, name);
		HomeService.sendHomeList(player);
		return changed ? 1 : 0;
	}

	private static int teleport(CommandContext<CommandSourceStack> context, String name)
			throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		return HomeService.teleportHome(player, name) ? 1 : 0;
	}

	/**
	 * /home with no argument. Opening the GUI is preferred; clients without the mod fall back
	 * to the default home, or to the chat listing when that home does not exist.
	 */
	private static int bareHome(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		HomeGuiConfig config = HomeGuiConfig.get();

		if (config.openGuiOnBareHomeCommand && HomeService.canOpenGui(player)) {
			HomeService.openGui(player);
			return 1;
		}

		Home fallback = HomeManager.find(player.getUUID(), config.defaultHomeName);

		if (fallback != null) {
			return HomeService.teleportHome(player, fallback.name) ? 1 : 0;
		}

		HomeService.listHomes(player);
		return 1;
	}

	private static int openOrList(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();

		if (HomeService.canOpenGui(player)) {
			HomeService.openGui(player);
		} else {
			HomeService.listHomes(player);
		}

		return 1;
	}

	private static int reload(CommandContext<CommandSourceStack> context) {
		HomeGuiConfig.load();
		Localization.load();
		Sounds.forgetWarnings();

		CommandSourceStack source = context.getSource();
		source.sendSuccess(() -> Localization.message(source, Lang.CONFIG_RELOADED), true);
		return 1;
	}

	private static String name(CommandContext<CommandSourceStack> context) {
		return StringArgumentType.getString(context, NAME_ARGUMENT);
	}
}
