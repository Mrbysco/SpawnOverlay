package com.mrbysco.spawnoverlay.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import com.mrbysco.spawnoverlay.Reference;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class ModKeymaps {
	public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "category"));
	public static final KeyMapping TOGGLE_RENDER = new KeyMapping(getKey("toggle_render"), InputConstants.KEY_F7, CATEGORY);
	public static final KeyMapping TOGGLE_OPTIMIZER = new KeyMapping(getKey("toggle_optimizer"), InputConstants.KEY_F8, CATEGORY);
	public static final KeyMapping TOGGLE_STRUCTURE_MODE = new KeyMapping(getKey("toggle_structure_mode"), InputConstants.KEY_F9, CATEGORY);

	private static String getKey(String name) {
		return String.join(".", "key", Reference.MOD_ID, name);
	}
}
