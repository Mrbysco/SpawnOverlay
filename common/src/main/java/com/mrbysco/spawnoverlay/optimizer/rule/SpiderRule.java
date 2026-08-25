package com.mrbysco.spawnoverlay.optimizer.rule;

import net.minecraft.world.entity.EntityType;

public class SpiderRule implements SpawnRule {
	@Override
	public EntityType<?> entityType() {
		return EntityType.SPIDER;
	}

	@Override
	public int requiredWidth() {
		return 3;
	}

	@Override
	public int requiredHeight() {
		return 1;
	}
}
