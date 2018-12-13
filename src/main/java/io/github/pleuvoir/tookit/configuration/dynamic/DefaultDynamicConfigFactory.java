package io.github.pleuvoir.tookit.configuration.dynamic;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultDynamicConfigFactory implements DynamicConfigFactory {

	private final ConfigWatcher watcher = new ConfigWatcher();
	private final ConcurrentMap<String, DefaultDynamicConfig> configs = new ConcurrentHashMap<>();

	@Override
	public DynamicConfig create(final String name, final boolean failOnNotExist) {
		if (configs.containsKey(name)) {
			return configs.get(name);
		}
		return doCreate(name, failOnNotExist);
	}

	private DefaultDynamicConfig doCreate(final String name, final boolean failOnNotExist) {
		final DefaultDynamicConfig prev = configs.putIfAbsent(name, new DefaultDynamicConfig(name, failOnNotExist));
		final DefaultDynamicConfig config = configs.get(name);
		if (prev == null) {
			watcher.addWatch(config);
			config.onConfigModified();
		}
		return config;
	}
}