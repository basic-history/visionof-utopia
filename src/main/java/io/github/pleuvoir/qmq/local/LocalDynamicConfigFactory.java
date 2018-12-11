package io.github.pleuvoir.qmq.local;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.github.pleuvoir.qmq.DynamicConfig;
import io.github.pleuvoir.qmq.DynamicConfigFactory;

public class LocalDynamicConfigFactory implements DynamicConfigFactory {
	private final ConfigWatcher watcher = new ConfigWatcher();
	private final ConcurrentMap<String, LocalDynamicConfig> configs = new ConcurrentHashMap<>();

	@Override
	public DynamicConfig create(final String name, final boolean failOnNotExist) {
		if (configs.containsKey(name)) {
			return configs.get(name);
		}

		return doCreate(name, failOnNotExist);
	}

	private LocalDynamicConfig doCreate(final String name, final boolean failOnNotExist) {
		final LocalDynamicConfig prev = configs.putIfAbsent(name, new LocalDynamicConfig(name, failOnNotExist));
		final LocalDynamicConfig config = configs.get(name);
		if (prev == null) {
			watcher.addWatch(config);
			config.onConfigModified();
		}
		return config;
	}
}