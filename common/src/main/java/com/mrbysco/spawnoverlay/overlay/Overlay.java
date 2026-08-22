package com.mrbysco.spawnoverlay.overlay;

public class Overlay {
	public final int x;
	public final int z;
	public final double y;
	public final int blockLight;
	public final OverlayColor overlayColor;

	public Overlay(int x, double y, int z, int blockLight, OverlayColor overlayColor) {
		this.x = x;
		this.y = y + 0.005;
		this.z = z;
		this.blockLight = blockLight;
		this.overlayColor = overlayColor;
	}
}
