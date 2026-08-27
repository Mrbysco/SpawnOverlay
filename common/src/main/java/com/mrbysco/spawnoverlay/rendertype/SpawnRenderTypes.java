package com.mrbysco.spawnoverlay.rendertype;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public abstract class SpawnRenderTypes extends RenderType {

	public SpawnRenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
		super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
	}

	public static final RenderType.CompositeRenderType TRANSLUCENT = create(
			"spawnoverlay:translucent",
			DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 1536,
			false,
			true,
			RenderType.CompositeState.builder()
					.setShaderState(POSITION_COLOR_SHADER)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setCullState(NO_CULL)
					.createCompositeState(false)
	);
}
