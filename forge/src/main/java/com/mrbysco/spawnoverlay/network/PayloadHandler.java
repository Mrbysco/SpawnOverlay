package com.mrbysco.spawnoverlay.network;

import com.mrbysco.spawnoverlay.structure.StructureData;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PayloadHandler {
	private static final PayloadHandler INSTANCE = new PayloadHandler();

	public static PayloadHandler getInstance() {
		return INSTANCE;
	}


	public void handleStructureData(final SetKnownStructurePayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
					StructureData.setKnownStructure(payload.structure().orElse(null), payload.boxes().orElse(null));
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("spawnoverlay.networking.set_known_structure.failed", e.getMessage()));
					return null;
				});
	}

}
