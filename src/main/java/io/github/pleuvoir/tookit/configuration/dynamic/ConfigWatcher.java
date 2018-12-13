package io.github.pleuvoir.tookit.configuration.dynamic;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 配置文件监测线程，当文件发生变化时会重载配置，并广播监听器
 * @author pleuvoir
 *
 */
public class ConfigWatcher {

	private final CopyOnWriteArrayList<Watch> watches = new CopyOnWriteArrayList<>();

	private final ScheduledExecutorService watcherExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
		Thread t = new Thread(r, "config-watcher");
		t.setDaemon(true);
		return t;
	});

	ConfigWatcher() {
		start();
	}

	private void start() {
		watcherExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				for (Watch watch : watches) {
					try {
						checkWatch(watch);
					} catch (Exception e) {
						throw new RuntimeException("check config file failed", e);
					}
				}
			}
		}, 10, 10, TimeUnit.SECONDS);
	}

	private void checkWatch(final Watch watch) {
		final DefaultDynamicConfig config = watch.getConfig();
		final long lastModified = config.getLastModified();
		if (lastModified == watch.getLastModified()) {
			return;
		}
		watch.setLastModified(lastModified);
		config.onConfigModified();
	}

	void addWatch(final DefaultDynamicConfig config) {
		final Watch watch = new Watch(config);
		watch.setLastModified(config.getLastModified());
		watches.add(watch);
	}

	private static final class Watch {

		private final DefaultDynamicConfig config;

		private volatile long lastModified;

		private Watch(final DefaultDynamicConfig config) {
			this.config = config;
		}

		public DefaultDynamicConfig getConfig() {
			return config;
		}

		long getLastModified() {
			return lastModified;
		}

		void setLastModified(final long lastModified) {
			this.lastModified = lastModified;
		}
	}
}
