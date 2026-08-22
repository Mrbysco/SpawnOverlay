package com.mrbysco.spawnoverlay.overlay;

import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.util.ColorParser;

public enum OverlayColor {
	ALWAYS_SPAWN,
	NIGHT_SPAWN,
	PHANTOM_SPAWN,
	PREVENT_SPAWN;

	private int color = 0xFFFFFFFF;

	public int color() {
		return this.color;
	}

	public static void updateColors() {
		ALWAYS_SPAWN.color = ColorParser.parse(OverlayConfig.CLIENT.alwaysSpawningColor.get());
		NIGHT_SPAWN.color = ColorParser.parse(OverlayConfig.CLIENT.nightSpawningColor.get());
		PHANTOM_SPAWN.color = ColorParser.parse(OverlayConfig.CLIENT.phantomSpawningColor.get());
		PREVENT_SPAWN.color = ColorParser.parse(OverlayConfig.CLIENT.preventSpawningColor.get());
	}
}
