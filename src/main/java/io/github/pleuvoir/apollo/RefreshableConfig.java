package io.github.pleuvoir.apollo;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Strings;


public abstract class RefreshableConfig {

	private static Logger logger = LoggerFactory.getLogger(RefreshableConfig.class);

	private static final String LIST_SEPARATOR = ",";
	// TimeUnit: second
	private static final int CONFIG_REFRESH_INTERVAL = 10;	

	@Autowired
	private ConfigurableEnvironment environment;

	private List<RefreshablePropertySource> propertySources;

	/**
	 * register refreshable property source.
	 * Notice: The front property source has higher priority.
	 */
	protected abstract List<RefreshablePropertySource> getRefreshablePropertySources();

	// 第一次启动时便执行一次
	@PostConstruct
	public void setup() {

		// 如何获取数据，留给子类去实现   并不断的进行刷新
		propertySources = getRefreshablePropertySources();
		if (CollectionUtils.isEmpty(propertySources)) {
			throw new IllegalStateException("Property sources can not be empty.");
		}

		// add property source to environment
		for (RefreshablePropertySource propertySource : propertySources) {
			propertySource.refresh();
			environment.getPropertySources().addLast(propertySource);
		}

		// task to update configs
		
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "refresh-config-watcher");
			t.setDaemon(true);
			return t;
		});
		

		executorService.scheduleWithFixedDelay(() -> {
			try {
				propertySources.forEach(RefreshablePropertySource::refresh);
			} catch (Throwable t) {
				logger.error("Refresh configs failed.", t);
			}
		}, CONFIG_REFRESH_INTERVAL, CONFIG_REFRESH_INTERVAL, TimeUnit.SECONDS);
	}

	public int getIntProperty(String key, int defaultValue) {
		try {
			String value = getValue(key);
			return value == null ? defaultValue : Integer.parseInt(value);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public boolean getBooleanProperty(String key, boolean defaultValue) {
		try {
			String value = getValue(key);
			return value == null ? defaultValue : "true".equals(value);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public String[] getArrayProperty(String key, String[] defaultValue) {
		try {
			String value = getValue(key);
			return Strings.isNullOrEmpty(value) ? defaultValue : value.split(LIST_SEPARATOR);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public String getValue(String key, String defaultValue) {
		try {
			return environment.getProperty(key, defaultValue);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public String getValue(String key) {
		return environment.getProperty(key);
	}

}