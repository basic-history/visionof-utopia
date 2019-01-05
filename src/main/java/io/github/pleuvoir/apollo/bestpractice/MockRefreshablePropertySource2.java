package io.github.pleuvoir.apollo.bestpractice;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

/**
 * 模拟刷新源，此类实现了动态刷新的逻辑
 * @author pleuvoir
 *
 */
@Component
public class MockRefreshablePropertySource2 extends RefreshablePropertySource {

	private final Log logger = LogFactory.getLog(getClass());

	public MockRefreshablePropertySource2(String name, Map<String, Object> source) {
		super(name, source);
	}

	/**
	 * 提供默认的构造方法，方便 spring 实例化
	 */
	public MockRefreshablePropertySource2() {
		super("mockRefreshablePropertySource2", Maps.newConcurrentMap());
	}

	AtomicLong mock = new AtomicLong(-1000);

	/*
	 * 模拟刷新逻辑的实现
	 * @see io.github.pleuvoir.chapter12.refreshconfig.
	 * AbstractRefreshablePropertySource#refresh()
	 */
	@Override
	protected void refresh() {

		Map<String, Object> newConfigs = Maps.newHashMap();
		
		// 这里模拟数据库的数据发生了变化
		newConfigs.put("mockKey2", mock.getAndIncrement());
				
		// put to environment
		for (Map.Entry<String, Object> config : newConfigs.entrySet()) {
			String key = config.getKey();
			Object value = config.getValue();

			if (this.source.get(key) == null) {
				logger.info("加载模拟配置 : " + key + " value" + value);
			} else if (!Objects.equals(this.source.get(key), value)) {
				logger.info("加载模拟配置，监测到配置已更新 : " + key + " = " + value + ". Old value = " + this.source.get(key));
			}
			// 更新
			this.source.put(key, value);
		}
	}

}
