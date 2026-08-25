package com.mrbysco.spawnoverlay.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import com.mrbysco.spawnoverlay.optimizer.OptimizerInstance;
import com.mrbysco.spawnoverlay.overlay.OverlayInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class KeybindHandler {

	public static void onKeyPress(Minecraft minecraft) {
		if (minecraft.screen != null) return;

		if (InputConstants.isKeyDown(minecraft.getWindow(), 292)) return;

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
				mc.player.sendOverlayMessage(Component.translatable("spawnoverlay.toggle_structure_mode.message", toggleComponent));
			}
		}
	}

}
