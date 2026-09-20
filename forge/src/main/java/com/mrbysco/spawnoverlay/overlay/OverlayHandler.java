package com.mrbysco.spawnoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.spawnoverlay.render.OverlayRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

@EventBusSubscriber(Dist.CLIENT)
public class OverlayHandler {
	@SubscribeEvent
	public static void submitCustomGeometry(SubmitCustomGeometryEvent event) {
		SubmitNodeCollector nodeCollector = event.getSubmitNodeCollector();
		PoseStack poseStack = event.getPoseStack();
		Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;
		OverlayRenderer.submitCustomGeometry(nodeCollector, poseStack, camera);
	}
}
