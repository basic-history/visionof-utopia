package io.github.pleuvoir.apollo;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 继承了 RefreshableConfig 后变拥有了刷新的功能，提供刷新源即可
 * @author pleuvoir
 *
 */
@Component
public class BizConfig extends RefreshableConfig {


	// 从数据库中获取到配置
	@Autowired
	private BizDBPropertySource propertySource;

	@Override
	protected List<RefreshablePropertySource> getRefreshablePropertySources() {
		return Collections.singletonList(propertySource);
	}

}
