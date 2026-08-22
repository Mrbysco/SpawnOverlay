package com.mrbysco.spawnoverlay.optimizer.rule;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;

public interface SpawnRule {
	EntityType<?> entityType();

	int requiredWidth();

	int requiredHeight();

	default BlockPos findGround(ClientLevel level, int x, int z, int minY, int maxY) {
		for (int y = maxY; y >= minY; y--) {
			BlockPos ground = new BlockPos(x, y, z);
			if (!level.getBlockState(ground).isFaceSturdy(level, ground, Direction.UP))
				continue;
			if (hasClearSpace(level, ground))
				return ground;
		}
		return null;
	}

	private boolean hasClearSpace(ClientLevel level, BlockPos ground) {
		int half = requiredWidth() / 2;
		for (int dx = -half; dx <= half; dx++) {
			for (int dz = -half; dz <= half; dz++) {
				for (int dy = 1; dy <= requiredHeight(); dy++) {
					BlockPos check = ground.offset(dx, dy, dz);
					if (!level.getBlockState(check).getCollisionShape(level, check).isEmpty())
						return false;
				}
			}
		}
		return true;
	}

	default boolean isDeniedBy(BlockPos candidate, BlockPos slabAbovePos) {
		int half = requiredWidth() / 2;
		return Math.abs(candidate.getX() - slabAbovePos.getX()) <= half
				&& Math.abs(candidate.getZ() - slabAbovePos.getZ()) <= half
				&& slabAbovePos.getY() > candidate.getY()
				&& slabAbovePos.getY() <= candidate.getY() + requiredHeight();
	}
}
