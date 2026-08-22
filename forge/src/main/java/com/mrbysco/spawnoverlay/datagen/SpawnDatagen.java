package com.mrbysco.spawnoverlay.datagen;

import com.mrbysco.spawnoverlay.Reference;
import com.mrbysco.spawnoverlay.SpawnOverlayClient;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jspecify.annotations.Nullable;

@EventBusSubscriber(Dist.CLIENT)
public class SpawnDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();

		generator.addProvider(true, new SpawnLanguageProvider(packOutput));
	}

	public static class SpawnLanguageProvider extends LanguageProvider {

		public SpawnLanguageProvider(PackOutput output) {
			super(output, Reference.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			add("key.category.spawnoverlay.category", "Spawn Overlay");
			add("key.spawnoverlay.toggle_render", "Toggle Render");
			add("key.spawnoverlay.toggle_optimizer", "Toggle Optimizer");
			add("key.spawnoverlay.toggle_structure_mode", "Toggle Structure Mode");

			add("spawnoverlay.toggle_structure_mode.message", "Structure Mode: %s");
			add("spawnoverlay.toggle_structure_mode.enabled", "Enabled");
			add("spawnoverlay.toggle_structure_mode.disabled", "Disabled");

			addConfig("overlay", "Overlay", "Overlay Settings");
			addConfig("pollInterval", "Poll Interval", "The interval in milliseconds between overlay updates (Default: 200)");
			addConfig("chunkRadius", "Chunk Radius", "The radius of chunks to display the overlay (Default: 3)");
			addConfig("overlayType", "Overlay Type", "The type of overlay to display (Default: OUTLINE)");
			addConfig("ignorePeacefulBiomes", "Ignore Peaceful Biomes", "Ignore peaceful biomes when displaying the overlay (Default: true)");
			addConfig("optimizerType", "Optimizer Type", "The type of slab optimizer to use (Default: SPIDER)");

			addConfig("toggles", "Toggles", "Toggle Settings");
			addConfig("showAlwaysSpawning", "Show Always Spawning", "Show positions mobs can spawn regardless of time (Default: false)");
			addConfig("showNightSpawning", "Show Night Spawning", "Show positions mobs can spawn at night (Default: true)");
			addConfig("showPhantomSpawning", "Show Phantom Spawning", "Show positions phantoms can spawn (Default: false)");
			addConfig("showPreventSpawning", "Show Prevent Spawning", "Show positions where spawning is prevented (Default: false)");

			addConfig("color", "Color", "Color Settings");
			addConfig("alwaysSpawningColor", "Always Spawning Color", "The hex color (with alpha) used to show positions mobs can spawn regardless of time (Default: #228B22B3 (4D = 30% opacity))");
			addConfig("nightSpawningColor", "Night Spawning Color", "The hex color (with alpha) used to show positions mobs can spawn at night (Default: #780606B3 (4D = 30% opacity))");
			addConfig("phantomSpawningColor", "Phantom Spawning Color", "The hex color (with alpha) used to show positions phantoms can spawn (Default: #3e5089B3 (4D = 30% opacity))");
			addConfig("preventSpawningColor", "Prevent Spawning Color", "The hex color (with alpha) used to show positions where spawning is prevented (Default: #FFFFFFB3 (4D = 30% opacity))");
			addConfig("optimizerColor", "Optimizer Color", "The hex color (with alpha) used to show where to place slabs (Default: #808080B3 (4D = 30% opacity))");
		}

		/**
		 * Add the translation for a config entry
		 *
		 * @param path        The path of the config entry
		 * @param name        The name of the config entry
		 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
		 */
		private void addConfig(String path, String name, @Nullable String description) {
			this.add(Reference.MOD_ID + ".configuration." + path, name);
			if (description != null && !description.isEmpty())
				this.add(Reference.MOD_ID + ".configuration." + path + ".tooltip", description);
		}
	}
}
