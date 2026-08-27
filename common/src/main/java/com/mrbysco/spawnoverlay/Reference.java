package com.mrbysco.spawnoverlay;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Reference {
	public static final String MOD_ID = "spawnoverlay";
	public static final String MOD_NAME = "SpawnOverlay";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}