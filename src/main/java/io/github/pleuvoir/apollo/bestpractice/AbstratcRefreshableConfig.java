package io.github.pleuvoir.apollo.bestpractice;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

/**
 * 可刷新的配置，通过守护线程定时更新
 * @author pleuvoir
 *
 */
public abstract class AbstratcRefreshableConfig implements InitializingBean {


	@Autowired
	private ConfigurableEnvironment configurableEnvironment;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<RefreshablePropertySource> propertySources = this.getRefreshablePropertySources();

		Assert.notNull(propertySources, "refreshablePropertySources must not be null");

		// 启动时先加载一次
		propertySources.forEach(source -> {
			source.refresh();
			configurableEnvironment.getPropertySources().addLast(source);
		});

		// 定时监测线程，默认是 10 秒监测一次，可以改为动态配置的
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "refresh-config-watcher");
			t.setDaemon(true);
			return t;
		});

		executorService.scheduleWithFixedDelay(() -> {
			try {
				propertySources.forEach(RefreshablePropertySource::refresh);
			} catch (Throwable t) {
				t.getStackTrace();
			}
		}, 0L, 10L, TimeUnit.SECONDS);
	}

	protected abstract List<RefreshablePropertySource> getRefreshablePropertySources();

}
