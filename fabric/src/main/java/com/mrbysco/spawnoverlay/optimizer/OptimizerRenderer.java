package com.mrbysco.spawnoverlay.optimizer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.rendertype.SpawnRenderTypes;
import com.mrbysco.spawnoverlay.util.ColorParser;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

public class OptimizerRenderer {

	public static void submitCustomGeometry(LevelRenderContext context) {
		if (OptimizerInstance.active) {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player == null)
				return;

			SubmitNodeCollector nodeCollector = context.submitNodeCollector();
			PoseStack poseStack = context.poseStack();
			Vec3 camera = context.levelState().cameraRenderState.pos;

			poseStack.pushPose();
			poseStack.translate(-camera.x, -camera.y, -camera.z);

			RenderType renderType = SpawnRenderTypes.TRANSLUCENT;
			int color = ColorParser.parse(OverlayConfig.CLIENT.optimizerColor.get());

			nodeCollector.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
				var overlays = OptimizerInstance.poller.positions;
				for (BlockPos slabPosition : overlays) {
					drawSlab(vertexConsumer, pose, slabPosition, color);
				}
			});

			poseStack.popPose();
		}
	}

	private static void drawSlab(VertexConsumer vertexConsumer, PoseStack.Pose pose, BlockPos pos,
	                             int color) {
		float x1 = pos.getX();
		float x2 = pos.getX() + 1;
		float y1 = pos.getY();
		float y2 = pos.getY() + 0.5F;
		float z1 = pos.getZ();
		float z2 = pos.getZ() + 1;

		// Top
		quad(vertexConsumer, pose, x1, y2, z1, x2, y2, z1, x2, y2, z2, x1, y2, z2, 0, 1, 0, color);
		// Bottom
		quad(vertexConsumer, pose, x1, y1, z2, x2, y1, z2, x2, y1, z1, x1, y1, z1, 0, -1, 0, color);
		// North
		quad(vertexConsumer, pose, x1, y2, z1, x2, y2, z1, x2, y1, z1, x1, y1, z1, 0, 0, -1, color);
		// South
		quad(vertexConsumer, pose, x2, y2, z2, x1, y2, z2, x1, y1, z2, x2, y1, z2, 0, 0, 1, color);
		// West
		quad(vertexConsumer, pose, x1, y2, z2, x1, y2, z1, x1, y1, z1, x1, y1, z2, -1, 0, 0, color);
		// East
		quad(vertexConsumer, pose, x2, y2, z1, x2, y2, z2, x2, y1, z2, x2, y1, z1, 1, 0, 0, color);
	}

	private static void quad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
	                         float x1, float y1, float z1,
	                         float x2, float y2, float z2,
	                         float x3, float y3, float z3,
	                         float x4, float y4, float z4,
	                         float nx, float ny, float nz,
	                         int color) {
		int abgr = ARGB.toABGR(color);
		vertexConsumer.addVertex(pose, x1, y1, z1)
				.setNormal(pose, nx, ny, nz)
				.setColor(abgr);
		vertexConsumer.addVertex(pose, x2, y2, z2)
				.setNormal(pose, nx, ny, nz)
				.setColor(abgr);
		vertexConsumer.addVertex(pose, x3, y3, z3)
				.setNormal(pose, nx, ny, nz)
				.setColor(abgr);
		vertexConsumer.addVertex(pose, x4, y4, z4)
				.setNormal(pose, nx, ny, nz)
				.setColor(abgr);
	}
}
