package com.mrbysco.spawnoverlay.platform;

import com.mrbysco.spawnoverlay.Reference;
import com.mrbysco.spawnoverlay.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

public class Services {
	public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

	public static <T> T load(Class<T> clazz) {

		final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
				.findFirst()
				.orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
		Reference.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
		return loadedService;
	}
}
