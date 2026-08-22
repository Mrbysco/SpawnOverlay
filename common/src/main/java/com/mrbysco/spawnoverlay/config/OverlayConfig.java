package com.mrbysco.spawnoverlay.config;

import com.mrbysco.spawnoverlay.optimizer.OptimizerType;
import com.mrbysco.spawnoverlay.overlay.OverlayType;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class OverlayConfig {

	public static class Client {
		public final ModConfigSpec.IntValue pollInterval;
		public final ModConfigSpec.IntValue chunkRadius;
		public final ModConfigSpec.EnumValue<OverlayType> overlayType;

		public final ModConfigSpec.BooleanValue ignorePeacefulBiomes;

		public final ModConfigSpec.BooleanValue showAlwaysSpawning;
		public final ModConfigSpec.BooleanValue showNightSpawning;
		public final ModConfigSpec.BooleanValue showPhantomSpawning;
		public final ModConfigSpec.BooleanValue showPreventSpawning;

		public final ModConfigSpec.ConfigValue<? extends String> alwaysSpawningColor;
		public final ModConfigSpec.ConfigValue<? extends String> nightSpawningColor;
		public final ModConfigSpec.ConfigValue<? extends String> phantomSpawningColor;
		public final ModConfigSpec.ConfigValue<? extends String> preventSpawningColor;
		public final ModConfigSpec.ConfigValue<? extends String> optimizerColor;

		public final ModConfigSpec.EnumValue<OptimizerType> optimizerType;

		Client(ModConfigSpec.Builder builder) {
			builder.comment("Overlay settings")
					.push("overlay");

			pollInterval = builder
					.comment("The interval in milliseconds between overlay updates (Default: 200)")
					.defineInRange("pollInterval", 200, 1, 30000);

			chunkRadius = builder
					.comment("The radius of chunks to display the overlay (Default: 3)")
					.defineInRange("chunkRadius", 3, 1, 16);

			overlayType = builder
					.comment("The type of overlay to display (Default: OUTLINE)")
					.defineEnum("overlayType", OverlayType.OUTLINE);

			ignorePeacefulBiomes = builder
					.comment("Ignore peaceful biomes when displaying the overlay (Default: true)")
					.define("ignorePeacefulBiomes", true);

			builder.comment("Toggle settings")
					.push("toggles");

			showAlwaysSpawning = builder
					.comment("Show positions mobs can spawn regardless of time (Default: false)")
					.define("showAlwaysSpawning", false);
			showNightSpawning = builder
					.comment("Show positions mobs can spawn at night (Default: true)")
					.define("showNightSpawning", true);
			showPhantomSpawning = builder
					.comment("Show positions phantoms can spawn (Default: false)")
					.define("showPhantomSpawning", false);
			showPreventSpawning = builder
					.comment("Show positions where spawning is prevented (Default: false)")
					.define("showPreventSpawning", false);

			builder.pop();


			builder.comment("Color settings")
					.push("color");

			alwaysSpawningColor = builder
					.comment("The hex color (with alpha) used to show positions mobs can spawn regardless of time (Default: #228B22B3 (4D = 30% opacity))")
					.define("alwaysSpawningColor", "#228B224D", OverlayConfig::isHexColor);

			nightSpawningColor = builder
					.comment("The hex color (with alpha) used to show positions mobs can spawn at night (Default: #780606B3 (4D = 30% opacity))")
					.define("nightSpawningColor", "#7806064D", OverlayConfig::isHexColor);

			phantomSpawningColor = builder
					.comment("The hex color (with alpha) used to show positions phantoms can spawn (Default: #3e5089B3 (4D = 30% opacity))")
					.define("phantomSpawningColor", "#3e50894D", OverlayConfig::isHexColor);

			preventSpawningColor = builder
					.comment("The hex color (with alpha) used to show positions where spawning is prevented (Default: #FFFFFFB3 (4D = 30% opacity))")
					.define("preventSpawningColor", "#FFFFFF4D", OverlayConfig::isHexColor);

			optimizerColor = builder
					.comment("The hex color (with alpha) used to show where to place slabs (Default: #808080B3 (4D = 30% opacity))")
					.define("optimizerColor", "#8080804D", OverlayConfig::isHexColor);

			builder.pop();

			optimizerType = builder
					.comment("The type of slab optimizer to use (Default: SPIDER)")
					.defineEnum("optimizerType", OptimizerType.SPIDER);

			builder.pop();
		}
	}

	private static boolean isHexColor(Object object) {
		if (object instanceof String str) {
			return str.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$");
		}
		return false;
	}

	public static final ModConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}
}
