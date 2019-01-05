package io.github.pleuvoir.apollo.bestpractice;

import java.util.Map;

import org.springframework.core.env.MapPropertySource;

/**
 * 可刷新的配置源
 * @author pleuvoir
 *
 */
public abstract class RefreshablePropertySource extends MapPropertySource {

	public RefreshablePropertySource(String name, Map<String, Object> source) {
		super(name, source);
	}

	@Override
	public Object getProperty(String name) {
		return this.source.get(name);
	}

	protected abstract void refresh();

}
