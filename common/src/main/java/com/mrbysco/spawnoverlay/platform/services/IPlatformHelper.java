package com.mrbysco.spawnoverlay.platform.services;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface IPlatformHelper {
	/**
	 * Get the conventional peaceful biome tag
	 *
	 * @return the peaceful biome tag
	 */
	TagKey<Biome> getPeacefulTag();
}
