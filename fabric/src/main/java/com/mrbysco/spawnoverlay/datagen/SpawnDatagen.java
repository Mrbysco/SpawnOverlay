package com.mrbysco.spawnoverlay.datagen;

import com.mrbysco.spawnoverlay.Reference;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SpawnDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();

		pack.addProvider(SpawnLanguageProvider::new);
	}

	public static class SpawnLanguageProvider extends FabricLanguageProvider {

		public SpawnLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
			builder.add("key.category.spawnoverlay.category", "Spawn Overlay");
			builder.add("key.spawnoverlay.toggle_render", "Toggle Render");
			builder.add("key.spawnoverlay.toggle_optimizer", "Toggle Optimizer");
			builder.add("key.spawnoverlay.toggle_structure_mode", "Toggle Structure Mode");

			builder.add("spawnoverlay.toggle_structure_mode.message", "Structure Mode: %s");
			builder.add("spawnoverlay.toggle_structure_mode.enabled", "Enabled");
			builder.add("spawnoverlay.toggle_structure_mode.disabled", "Disabled");

			addConfig(builder, "overlay", "Overlay", "Overlay Settings");
			addConfig(builder, "pollInterval", "Poll Interval", "The interval in milliseconds between overlay updates (Default: 200)");
			addConfig(builder, "chunkRadius", "Chunk Radius", "The radius of chunks to display the overlay (Default: 3)");
			addConfig(builder, "overlayType", "Overlay Type", "The type of overlay to display (Default: OUTLINE)");
			addConfig(builder, "ignorePeacefulBiomes", "Ignore Peaceful Biomes", "Ignore peaceful biomes when displaying the overlay (Default: true)");
			addConfig(builder, "optimizerType", "Optimizer Type", "The type of slab optimizer to use (Default: SPIDER)");

			addConfig(builder, "toggles", "Toggles", "Toggle Settings");
			addConfig(builder, "showAlwaysSpawning", "Show Always Spawning", "Show positions mobs can spawn regardless of time (Default: false)");
			addConfig(builder, "showNightSpawning", "Show Night Spawning", "Show positions mobs can spawn at night (Default: true)");
			addConfig(builder, "showPhantomSpawning", "Show Phantom Spawning", "Show positions phantoms can spawn (Default: false)");
			addConfig(builder, "showPreventSpawning", "Show Prevent Spawning", "Show positions where spawning is prevented (Default: false)");

			addConfig(builder, "color", "Color", "Color Settings");
			addConfig(builder, "alwaysSpawningColor", "Always Spawning Color", "The hex color (with alpha) used to show positions mobs can spawn regardless of time (Default: #228B22B3 (4D = 30% opacity))");
			addConfig(builder, "nightSpawningColor", "Night Spawning Color", "The hex color (with alpha) used to show positions mobs can spawn at night (Default: #780606B3 (4D = 30% opacity))");
			addConfig(builder, "phantomSpawningColor", "Phantom Spawning Color", "The hex color (with alpha) used to show positions phantoms can spawn (Default: #3e5089B3 (4D = 30% opacity))");
			addConfig(builder, "preventSpawningColor", "Prevent Spawning Color", "The hex color (with alpha) used to show positions where spawning is prevented (Default: #FFFFFFB3 (4D = 30% opacity))");
			addConfig(builder, "optimizerColor", "Optimizer Color", "The hex color (with alpha) used to show where to place slabs (Default: #808080B3 (4D = 30% opacity))");
		}

		/**
		 * Add the translation for a config entry
		 *
		 * @param builder     The TranslationBuilder to add the translation to
		 * @param path        The path of the config entry
		 * @param name        The name of the config entry
		 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
		 */
		private void addConfig(TranslationBuilder builder, String path, String name, @Nullable String description) {
			builder.add(Reference.MOD_ID + ".configuration." + path, name);
			if (description != null && !description.isEmpty())
				builder.add(Reference.MOD_ID + ".configuration." + path + ".tooltip", description);
		}
	}
}
