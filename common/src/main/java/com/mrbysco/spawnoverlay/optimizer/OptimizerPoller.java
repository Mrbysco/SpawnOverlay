package com.mrbysco.spawnoverlay.optimizer;

import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.optimizer.rule.SpawnRule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class OptimizerPoller extends Thread {

	public volatile List<BlockPos> positions = new ArrayList<>();

	@Override
	public void run() {
		while (true) {
			if (OptimizerInstance.active)
				update();
			try {
				sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void update() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		ClientLevel level = mc.level;

		int playerPosX = (int) Math.floor(mc.player.getX());
		int playerPosZ = (int) Math.floor(mc.player.getZ());
		int playerPosY = (int) Math.floor(mc.player.getY());
		int radius = 64;
		int maxY = playerPosY + 4;
		int minY = Math.max(playerPosY - 40, level.getMinBuildHeight());

		SpawnRule spawnRule = OverlayConfig.CLIENT.optimizerType.get().getRule();
		int requiredWidth = spawnRule.requiredWidth();
		int minX = ((playerPosX - radius) / requiredWidth) * requiredWidth;
		int maxX = ((playerPosX + radius) / requiredWidth) * requiredWidth;
		int minZ = ((playerPosZ - radius) / requiredWidth) * requiredWidth;
		int maxZ = ((playerPosZ + radius) / requiredWidth) * requiredWidth;

		List<BlockPos> slabPositions = new ArrayList<>();
		for (int x = minX; x <= maxX; x += requiredWidth) {
			for (int z = minZ; z <= maxZ; z += requiredWidth) {
				BlockPos ground = spawnRule.findGround(level, x, z, minY, maxY);
				if (ground != null) slabPositions.add(ground.above());
			}
		}

		positions = slabPositions;
	}
}