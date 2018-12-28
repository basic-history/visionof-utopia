package io.github.pleuvoir.apollo;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
