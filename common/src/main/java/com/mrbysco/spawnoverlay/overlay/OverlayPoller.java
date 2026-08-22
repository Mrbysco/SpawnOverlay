package com.mrbysco.spawnoverlay.overlay;

import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.structure.StructureData;
import com.mrbysco.spawnoverlay.util.SpawnChecks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class OverlayPoller extends Thread {

	public volatile ArrayList<Overlay>[][] overlays;

	@Override
	public void run() {
		int radius = 0;
		while (true) {
			int chunkRadius = updateRadius();
			radius = radius % chunkRadius + 1;
			if (OverlayInstance.active)
				update(radius, chunkRadius);
			try {
				sleep(OverlayConfig.CLIENT.pollInterval.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private int updateRadius() {
		int size = OverlayConfig.CLIENT.chunkRadius.get();
		if (overlays == null || overlays.length != size * 2 + 1) {
			overlays = new ArrayList[size * 2 + 1][size * 2 + 1];
			for (int i = 0; i < overlays.length; i++)
				for (int j = 0; j < overlays[i].length; j++)
					overlays[i][j] = new ArrayList<>();
		}
		return size;
	}

	private void update(int radius, int chunkRadius) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null)
			return;

		ClientLevel level = mc.level;
		int playerPosY = (int) Math.floor(mc.player.getY());
		int playerChunkX = mc.player.chunkPosition().x();
		int playerChunkZ = mc.player.chunkPosition().z();

		for (int chunkX = playerChunkX - radius; chunkX <= playerChunkX + radius; chunkX++) {
			for (int chunkZ = playerChunkZ - radius; chunkZ <= playerChunkZ + radius; chunkZ++) {
				LevelChunk chunk = level.getChunk(chunkX, chunkZ);
				if (!level.hasChunksAt(new BlockPos(chunk.getPos().getMinBlockX(), 70, chunk.getPos().getMinBlockZ()), new BlockPos(chunk.getPos().getMaxBlockX(), 70, chunk.getPos().getMaxBlockZ())))
					continue;
				ArrayList<Overlay> buffer = new ArrayList<>();
				for (int offsetX = 0; offsetX < 16; offsetX++) {
					for (int offsetZ = 0; offsetZ < 16; offsetZ++) {
						List<Vec3> positions = findSurfacePositions(level, chunk, chunkX, chunkZ, offsetX, offsetZ, playerPosY);
						if (positions.isEmpty()) continue;

						for (Vec3 pos : positions) {
							BlockPos blockPos = BlockPos.containing(pos);

							OverlayColor color;
							if (SpawnChecks.isAlwaysSpawnable(level, blockPos) && OverlayConfig.CLIENT.showAlwaysSpawning.get()) {
								color = OverlayColor.ALWAYS_SPAWN;
							} else if (SpawnChecks.isNightSpawnable(level, blockPos) && OverlayConfig.CLIENT.showNightSpawning.get()) {
								color = OverlayColor.NIGHT_SPAWN;
							} else if (SpawnChecks.isPhantomSpawnable(level, blockPos) && OverlayConfig.CLIENT.showPhantomSpawning.get()) {
								color = OverlayColor.PHANTOM_SPAWN;
							} else if (OverlayConfig.CLIENT.showPreventSpawning.get()) {
								color = OverlayColor.PREVENT_SPAWN;
							} else {
								continue;
							}

							if (OverlayInstance.structureMode) {
								if (!StructureData.hasKnownStructure() || !StructureData.isInsideStructure(blockPos)) {
									continue;
								}
							}

							int blockLight = level.getBrightness(LightLayer.BLOCK, blockPos);
							buffer.add(new Overlay(blockPos.getX(), pos.y(), blockPos.getZ(), blockLight, color));
						}
					}
				}

				int len = chunkRadius * 2 + 1;
				int arrayX = (chunkX % len + len) % len;
				int arrayZ = (chunkZ % len + len) % len;
				overlays[arrayX][arrayZ] = buffer;
			}
		}
	}

	private List<Vec3> findSurfacePositions(ClientLevel level, LevelChunk chunk, int chunkX, int chunkZ, int offsetX, int offsetZ, int playerPosY) {
		int posX = (chunkX << 4) + offsetX;
		int posZ = (chunkZ << 4) + offsetZ;
		int maxY = playerPosY + 4;
		int minY = Math.max(playerPosY - 40, level.getMinY());

		BlockState preBlockState;
		BlockState curBlockState = chunk.getBlockState(new BlockPos(offsetX, maxY, offsetZ));
		Block preBlock;
		Block curBlock = curBlockState.getBlock();
		BlockPos prePos;
		BlockPos curPos = new BlockPos(posX, maxY, posZ);

		List<Vec3> positions = new ArrayList<>();
		for (int posY = maxY - 1; posY >= minY; posY--) {
			preBlockState = curBlockState;
			curBlockState = chunk.getBlockState(new BlockPos(offsetX, posY, offsetZ));
			preBlock = curBlock;
			curBlock = curBlockState.getBlock();
			prePos = curPos;
			curPos = new BlockPos(posX, posY, posZ);

			if (curBlockState.isAir() || curBlock == Blocks.BEDROCK || curBlock == Blocks.BARRIER
					|| preBlockState.canOcclude()
					|| !preBlockState.getFluidState().isEmpty()
					|| preBlockState.isSignalSource()
					|| !curBlockState.isFaceSturdy(level, curPos, Direction.UP)
					|| BaseRailBlock.isRail(preBlockState)
					|| curBlockState.is(BlockTags.REPLACEABLE)) {
				continue;
			}

			double offsetY = 0;
			if (preBlock == Blocks.SNOW || curBlockState.is(BlockTags.WOOL_CARPETS)) {
				offsetY = preBlockState.getOffset(curPos).y;
				if (offsetY >= 0.15)
					continue; // Snow layer too high
			}

			positions.add(new Vec3(prePos.getX(), prePos.getY() + offsetY, prePos.getZ()));
		}

		return positions;
	}
}
