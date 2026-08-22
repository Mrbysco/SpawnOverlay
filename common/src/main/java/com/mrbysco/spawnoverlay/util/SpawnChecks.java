package com.mrbysco.spawnoverlay.util;

import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.platform.Services;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SpawnChecks {

	/**
	 * Check if the block at the position passes the light check for spawning
	 *
	 * @param level the client level
	 * @param pos   the block position
	 * @return true if the block passes the light check, false otherwise
	 */
	private static boolean passesLightCheck(ClientLevel level, BlockPos pos) {
		int blockLightLimit = level.dimensionType().monsterSpawnBlockLightLimit();
		if (blockLightLimit < 15 && level.getBrightness(LightLayer.BLOCK, pos) > blockLightLimit) {
			return false;
		}

		int brightness = level.getMaxLocalRawBrightness(pos, 11);
		return brightness <= level.dimensionType().monsterSpawnLightTest().maxInclusive();
	}

	/**
	 * Check if the block at the position is a valid spawn block for the entity type
	 *
	 * @param level    the client level
	 * @param abovePos the block position
	 * @param type     the entity type
	 * @return true if the block is a valid spawn block for the entity type, false otherwise
	 */
	private static boolean isValidSpawnBlock(ClientLevel level, BlockPos abovePos, EntityType<?> type) {
		BlockPos below = abovePos.below();
		return level.getBlockState(below).isValidSpawn(level, below, type);
	}

	/**
	 * Check if the block at the position is always spawnable for a zombie
	 *
	 * @param level the client level
	 * @param pos   the block position
	 * @return true if the block is always spawnable for a zombie, false otherwise
	 */
	public static boolean isAlwaysSpawnable(ClientLevel level, BlockPos pos) {
		boolean ignorePeacefulBiomes = OverlayConfig.CLIENT.ignorePeacefulBiomes.get();
		if (ignorePeacefulBiomes && level.getBiome(pos).is(Services.PLATFORM.getPeacefulTag())) return false;
		return passesLightCheck(level, pos) && isValidSpawnBlock(level, pos, EntityTypes.ZOMBIE);
	}

	/**
	 * Check if the block at the position is spawnable for a zombie during the night
	 *
	 * @param level the client level
	 * @param pos   the block position
	 * @return true if the block is spawnable for a zombie during the night, false otherwise
	 */
	public static boolean isNightSpawnable(ClientLevel level, BlockPos pos) {
		boolean ignorePeacefulBiomes = OverlayConfig.CLIENT.ignorePeacefulBiomes.get();
		if (ignorePeacefulBiomes && level.getBiome(pos).is(Services.PLATFORM.getPeacefulTag())) return false;
		return passesLightCheck(level, pos)
				&& isValidSpawnBlock(level, pos, EntityTypes.ZOMBIE);
	}

	/**
	 * Check if the block at the position is spawnable for a phantom
	 *
	 * @param level     the client level
	 * @param groundPos the block position
	 * @return true if the block is spawnable for a phantom, false otherwise
	 */
	public static boolean isPhantomSpawnable(ClientLevel level, BlockPos groundPos) {
		if (level.dimensionType().hasSkyLight() && (groundPos.getY() < level.getSeaLevel() || !level.canSeeSky(groundPos))) {
			return false;
		}

		for (int dy = 20; dy <= 34; dy++) {
			BlockPos spawnPos = groundPos.above(dy);
			BlockState state = level.getBlockState(spawnPos);
			FluidState fluid = level.getFluidState(spawnPos);

			if (NaturalSpawner.isValidEmptySpawnBlock(level, spawnPos, state, fluid, EntityTypes.PHANTOM)) {
				return true;
			}
		}
		return false;
	}
}
