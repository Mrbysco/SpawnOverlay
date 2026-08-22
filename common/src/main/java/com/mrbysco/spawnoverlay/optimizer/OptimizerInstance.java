package com.mrbysco.spawnoverlay.optimizer;

import com.mrbysco.spawnoverlay.Reference;

public class OptimizerInstance {
	public static OptimizerPoller poller;

	public static boolean active;

	public static void startPolling() {
		if (poller.isAlive()) return;
		try {
			poller.start();
		} catch (Exception e) {
			Reference.LOGGER.error("Failed to start poller", e);
			poller = new OptimizerPoller();
		}
	}
}
