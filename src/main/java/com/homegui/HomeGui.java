package com.homegui;

import com.homegui.command.HomeCommands;
import com.homegui.config.HomeGuiConfig;
import com.homegui.data.HomeManager;
import com.homegui.lang.Localization;
import com.homegui.net.HomeNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeGui implements ModInitializer {
	public static final String MOD_ID = "homegui";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		HomeGuiConfig.load();
		Localization.load();

		HomeNetworking.registerPayloads();
		HomeNetworking.registerServerReceivers();

		ServerLifecycleEvents.SERVER_STARTED.register(HomeManager::init);
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> HomeManager.shutdown());
		ServerTickEvents.END_SERVER_TICK.register(HomeService::tick);

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			var playerId = handler.player.getUUID();
			HomeManager.unload(playerId);
			HomeService.forget(playerId);
		});

		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> HomeCommands.register(dispatcher));

		LOGGER.info("HomeGUI started, {} home(s) per player.", HomeGuiConfig.get().maxHomes);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
