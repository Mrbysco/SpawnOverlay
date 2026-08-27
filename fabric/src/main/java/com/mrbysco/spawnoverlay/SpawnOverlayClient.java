package com.mrbysco.spawnoverlay;

import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.keybind.KeybindHandler;
import com.mrbysco.spawnoverlay.keybind.ModKeymaps;
import com.mrbysco.spawnoverlay.network.SetKnownStructurePayload;
import com.mrbysco.spawnoverlay.optimizer.OptimizerInstance;
import com.mrbysco.spawnoverlay.optimizer.OptimizerPoller;
import com.mrbysco.spawnoverlay.optimizer.OptimizerRenderer;
import com.mrbysco.spawnoverlay.overlay.OverlayColor;
import com.mrbysco.spawnoverlay.overlay.OverlayInstance;
import com.mrbysco.spawnoverlay.overlay.OverlayPoller;
import com.mrbysco.spawnoverlay.overlay.OverlayRenderer;
import com.mrbysco.spawnoverlay.structure.StructureData;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.neoforged.fml.config.ModConfig;

public class SpawnOverlayClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		NeoForgeConfigRegistry.INSTANCE.register(Reference.MOD_ID, ModConfig.Type.CLIENT, OverlayConfig.clientSpec);
		NeoForgeModConfigEvents.loading(Reference.MOD_ID).register(this::onLoadConfig);
		NeoForgeModConfigEvents.reloading(Reference.MOD_ID).register(this::onLoadConfig);

		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			setup();
			OverlayColor.updateColors();
		});

		WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
			OverlayRenderer.submitCustomGeometry(context);
			OptimizerRenderer.submitCustomGeometry(context);
		});

		ClientTickEvents.END_CLIENT_TICK.register(KeybindHandler::onKeyPress);

		KeyBindingHelper.registerKeyBinding(ModKeymaps.TOGGLE_RENDER);
		KeyBindingHelper.registerKeyBinding(ModKeymaps.TOGGLE_OPTIMIZER);
		KeyBindingHelper.registerKeyBinding(ModKeymaps.TOGGLE_STRUCTURE_MODE);

		ClientPlayNetworking.registerGlobalReceiver(SetKnownStructurePayload.ID, (payload, context) -> {
			StructureData.setKnownStructure(payload.structure().orElse(null), payload.boxes().orElse(null));
		});
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
