package com.mrbysco.spawnoverlay.overlay;

import com.mrbysco.spawnoverlay.Reference;

public class OverlayInstance {
	public static OverlayPoller poller;

	public static boolean active;
	public static boolean structureMode;

	public static void startPolling() {
		if (poller.isAlive()) return;
		try {
			poller.start();
		} catch (Exception e) {
			Reference.LOGGER.error("Failed to start poller", e);
			poller = new OverlayPoller();
		}
	}
}
