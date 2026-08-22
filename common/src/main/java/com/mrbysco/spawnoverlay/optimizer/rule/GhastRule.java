package com.mrbysco.spawnoverlay.optimizer.rule;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

public class GhastRule implements SpawnRule{
	@Override
	public EntityType<?> entityType() {
		return EntityTypes.GHAST;
	}

	@Override
	public int requiredWidth() {
		return 5;
	}

	@Override
	public int requiredHeight() {
		return 4;
	}
}
