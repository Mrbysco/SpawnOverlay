package com.mrbysco.spawnoverlay.platform;

import com.mrbysco.spawnoverlay.platform.services.IPlatformHelper;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public TagKey<Biome> getPeacefulTag() {
		return Tags.Biomes.NO_DEFAULT_MONSTERS;
	}
}
