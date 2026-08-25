package com.mrbysco.spawnoverlay.optimizer.rule;

import net.minecraft.world.entity.EntityType;

public class GhastRule implements SpawnRule{
	@Override
	public EntityType<?> entityType() {
		return EntityType.GHAST;
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
