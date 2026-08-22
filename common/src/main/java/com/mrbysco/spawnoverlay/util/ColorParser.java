package com.mrbysco.spawnoverlay.util;

public class ColorParser {
	public static int parse(String hex) {
		String h = hex.substring(1);
		if (h.length() == 6) h += "FF";
		return (int) Long.parseLong(h, 16);
	}
}
