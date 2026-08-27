package com.mrbysco.spawnoverlay.keybind;

import com.mrbysco.spawnoverlay.optimizer.OptimizerInstance;
import com.mrbysco.spawnoverlay.overlay.OverlayInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(Dist.CLIENT)
public class KeybindHandler {
	@SubscribeEvent
	public static void registerKeymapping(final RegisterKeyMappingsEvent event) {
		event.register(ModKeymaps.TOGGLE_RENDER);
		event.register(ModKeymaps.TOGGLE_OPTIMIZER);
		event.register(ModKeymaps.TOGGLE_STRUCTURE_MODE);
	}

	@SubscribeEvent
	public static void onKeyInputEvent(InputEvent.Key event) {
		Minecraft mc = Minecraft.getInstance();
		if (ModKeymaps.TOGGLE_RENDER.consumeClick()) {
			OverlayInstance.active = !OverlayInstance.active;
			OverlayInstance.startPolling();
		}

		if (ModKeymaps.TOGGLE_OPTIMIZER.consumeClick()) {
			OptimizerInstance.active = !OptimizerInstance.active;
			OptimizerInstance.startPolling();
		}

		if (ModKeymaps.TOGGLE_STRUCTURE_MODE.consumeClick()) {
			OverlayInstance.structureMode = !OverlayInstance.structureMode;
			if (mc.player != null) {
				Component toggleComponent = OverlayInstance.structureMode ?
						Component.translatable("spawnoverlay.toggle_structure_mode.enabled").withStyle(ChatFormatting.GREEN) :
						Component.translatable("spawnoverlay.toggle_structure_mode.disabled").withStyle(ChatFormatting.RED);
				mc.player.displayClientMessage(Component.translatable("spawnoverlay.toggle_structure_mode.message", toggleComponent), true);
			}
		}
	}

}
