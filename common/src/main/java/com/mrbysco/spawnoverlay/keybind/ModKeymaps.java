package com.mrbysco.spawnoverlay.keybind;

import com.mrbysco.spawnoverlay.Reference;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeymaps {
	public static final String CATEGORY = getKey("category");
	public static final KeyMapping TOGGLE_RENDER = new KeyMapping(getKey("toggle_render"), GLFW.GLFW_KEY_F7, CATEGORY);
	public static final KeyMapping TOGGLE_OPTIMIZER = new KeyMapping(getKey("toggle_optimizer"), GLFW.GLFW_KEY_F8, CATEGORY);
	public static final KeyMapping TOGGLE_STRUCTURE_MODE = new KeyMapping(getKey("toggle_structure_mode"), GLFW.GLFW_KEY_F9, CATEGORY);

	private static String getKey(String name) {
		return String.join(".", "key", Reference.MOD_ID, name);
	}
}
