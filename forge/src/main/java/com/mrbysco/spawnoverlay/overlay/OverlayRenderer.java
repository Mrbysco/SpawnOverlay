package com.mrbysco.spawnoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.rendertype.SpawnRenderTypes;
import com.mrbysco.spawnoverlay.util.ColorParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;

@EventBusSubscriber(Dist.CLIENT)
public class OverlayRenderer {
	@SubscribeEvent
	public static void onRenderWorldLast(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS && OverlayInstance.active) {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player == null)
				return;

			final Minecraft minecraft = Minecraft.getInstance();
			RenderBuffers renderBuffers = minecraft.renderBuffers();
			MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();
			PoseStack poseStack = event.getPoseStack();
			Vec3 camera = event.getCamera().getPosition();

			poseStack.pushPose();
			poseStack.translate(-camera.x, -camera.y, -camera.z);

			RenderType renderType = SpawnRenderTypes.TRANSLUCENT;
			OverlayType overlayType = OverlayConfig.CLIENT.overlayType.get();
			VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
			var overlays = OverlayInstance.poller.overlays;
			for (ArrayList<Overlay>[] overlay : overlays) {
				for (ArrayList<Overlay> overlayArrayList : overlay) {
					for (Overlay u : overlayArrayList) {
						drawOverlay(vertexConsumer, poseStack.last(), u, overlayType);
					}
				}
			}

			bufferSource.endBatch(renderType);
			poseStack.popPose();
		}
	}

	private static void drawOverlay(VertexConsumer vertexConsumer, PoseStack.Pose pose, Overlay u, OverlayType type) {
		int color = u.overlayColor.color();

		float y = (float) u.y;

		switch (type) {
			case OUTLINE -> {
				float thickness = 0.1F;
				addQuad(vertexConsumer, pose, u.x, y, u.z, u.x + 1, u.z + thickness, color);
				addQuad(vertexConsumer, pose, u.x, y, u.z + 1 - thickness, u.x + 1, u.z + 1, color);
				addQuad(vertexConsumer, pose, u.x, y, u.z + thickness, u.x + thickness, u.z + 1 - thickness, color);
				addQuad(vertexConsumer, pose, u.x + 1 - thickness, y, u.z + thickness, u.x + 1, u.z + 1 - thickness, color);
			}
			case INNER_SQUARE -> {
				float inset = 0.25F;
				addQuad(vertexConsumer, pose, u.x + inset, y, u.z + inset, u.x + 1 - inset, u.z + 1 - inset, color);
			}
			default -> addQuad(vertexConsumer, pose, u.x, y, u.z, u.x + 1, u.z + 1, color);
		}
	}

	private static void addQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
	                            float x1, float y, float z1, float x2, float z2,
	                            int color) {
		int abgr = ColorParser.toABGR(color);
		vertexConsumer.addVertex(pose, x1, y, z1)
				.setNormal(pose, 0.0F, 1.0F, 0.0F)
				.setColor(abgr);
		vertexConsumer.addVertex(pose, x1, y, z2)
				.setNormal(pose, 0.0F, 1.0F, 0.0F)
				.setColor(abgr);
		vertexConsumer.addVertex(pose, x2, y, z2)
				.setNormal(pose, 0.0F, 1.0F, 0.0F)
				.setColor(abgr);
		vertexConsumer.addVertex(pose, x2, y, z1)
				.setNormal(pose, 0.0F, 1.0F, 0.0F)
				.setColor(abgr);
	}
}
