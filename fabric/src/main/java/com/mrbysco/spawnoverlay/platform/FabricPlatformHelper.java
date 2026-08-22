package com.mrbysco.spawnoverlay.platform;

import com.mrbysco.spawnoverlay.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public TagKey<Biome> getPeacefulTag() {
		return ConventionalBiomeTags.NO_DEFAULT_MONSTERS;
	}
}
