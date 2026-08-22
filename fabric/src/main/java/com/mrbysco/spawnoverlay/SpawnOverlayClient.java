package com.mrbysco.spawnoverlay;

import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.keybind.KeybindHandler;
import com.mrbysco.spawnoverlay.keybind.ModKeymaps;
import com.mrbysco.spawnoverlay.optimizer.OptimizerInstance;
import com.mrbysco.spawnoverlay.optimizer.OptimizerPoller;
import com.mrbysco.spawnoverlay.optimizer.OptimizerRenderer;
import com.mrbysco.spawnoverlay.overlay.OverlayColor;
import com.mrbysco.spawnoverlay.overlay.OverlayInstance;
import com.mrbysco.spawnoverlay.overlay.OverlayPoller;
import com.mrbysco.spawnoverlay.overlay.OverlayRenderer;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.neoforged.fml.config.ModConfig;

public class SpawnOverlayClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConfigRegistry.INSTANCE.register(Reference.MOD_ID, ModConfig.Type.CLIENT, OverlayConfig.clientSpec);
		ModConfigEvents.loading(Reference.MOD_ID).register(this::onLoadConfig);
		ModConfigEvents.reloading(Reference.MOD_ID).register(this::onLoadConfig);

		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			setup();
			OverlayColor.updateColors();
		});

		LevelRenderEvents.COLLECT_SUBMITS.register(context -> {
			OverlayRenderer.submitCustomGeometry(context);
			OptimizerRenderer.submitCustomGeometry(context);
		});

		ClientTickEvents.END_CLIENT_TICK.register(KeybindHandler::onKeyPress);

		KeyMappingHelper.registerKeyMapping(ModKeymaps.TOGGLE_RENDER);
		KeyMappingHelper.registerKeyMapping(ModKeymaps.TOGGLE_OPTIMIZER);
		KeyMappingHelper.registerKeyMapping(ModKeymaps.TOGGLE_STRUCTURE_MODE);

	}

	private void setup() {
		OverlayInstance.poller = new OverlayPoller();
		OverlayInstance.startPolling();

		OptimizerInstance.poller = new OptimizerPoller();
		OptimizerInstance.startPolling();
	}

	private void onLoadConfig(ModConfig config) {
		Reference.LOGGER.debug("Loaded Spawn Overlay's config file {}", config.getFileName());
		OverlayColor.updateColors();
	}
}
