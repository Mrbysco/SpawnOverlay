package com.mrbysco.spawnoverlay;


import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.optimizer.OptimizerInstance;
import com.mrbysco.spawnoverlay.optimizer.OptimizerPoller;
import com.mrbysco.spawnoverlay.overlay.OverlayColor;
import com.mrbysco.spawnoverlay.overlay.OverlayInstance;
import com.mrbysco.spawnoverlay.overlay.OverlayPoller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Reference.MOD_ID, dist = Dist.CLIENT)
public class SpawnOverlayClient {

	public SpawnOverlayClient(IEventBus eventBus, ModContainer container) {
		container.registerConfig(ModConfig.Type.CLIENT, OverlayConfig.clientSpec);
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

		eventBus.addListener(this::setup);
		eventBus.addListener(this::onConfigLoad);
	}

	private void setup(final FMLClientSetupEvent event) {
		OverlayInstance.poller = new OverlayPoller();
		OverlayInstance.startPolling();

		OptimizerInstance.poller = new OptimizerPoller();
		OptimizerInstance.startPolling();
	}

	private void onConfigLoad(final ModConfigEvent event) {
		ModConfig config = event.getConfig();
		Reference.LOGGER.debug("Loaded Spawn Overlay's config file {}", config.getFileName());
		OverlayColor.updateColors();
	}
}
