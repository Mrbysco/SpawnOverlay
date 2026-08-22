package com.mrbysco.spawnoverlay.rendertype;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public class SpawnRenderTypes {

	public static final RenderType TRANSLUCENT = RenderType.create(
			"spawnoverlay:translucent",
			RenderSetup.builder(RenderPipelines.DEBUG_QUADS)
					.setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
					.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
					.createRenderSetup()
	);

}
