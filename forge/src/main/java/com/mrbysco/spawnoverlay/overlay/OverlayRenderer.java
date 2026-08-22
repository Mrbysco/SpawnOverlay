package com.mrbysco.spawnoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrbysco.spawnoverlay.config.OverlayConfig;
import com.mrbysco.spawnoverlay.rendertype.SpawnRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

import java.util.ArrayList;

@EventBusSubscriber(Dist.CLIENT)
public class OverlayRenderer {
	@SubscribeEvent
	public static void submitCustomGeometry(SubmitCustomGeometryEvent event) {
		if (OverlayInstance.active) {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player == null)
				return;

			SubmitNodeCollector nodeCollector = event.getSubmitNodeCollector();
			PoseStack poseStack = event.getPoseStack();
			Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;

			poseStack.pushPose();
			poseStack.translate(-camera.x, -camera.y, -camera.z);

			RenderType renderType = SpawnRenderTypes.TRANSLUCENT;
			OverlayType overlayType = OverlayConfig.CLIENT.overlayType.get();
			nodeCollector.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
				var overlays = OverlayInstance.poller.overlays;
				for (ArrayList<Overlay>[] overlay : overlays) {
					for (ArrayList<Overlay> overlayArrayList : overlay) {
						for (Overlay u : overlayArrayList) {
							drawOverlay(vertexConsumer, pose, u, overlayType);
						}
					}
				}
			});

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
		int abgr = ARGB.toABGR(color);
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
