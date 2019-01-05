package io.github.pleuvoir.apollo.bestpractice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DynamicAppConfig extends AbstratcRefreshableConfig {

	@Autowired
	private MockRefreshablePropertySource propertySource;
	@Autowired
	private MockRefreshablePropertySource2 propertySource2;

	@Override
	protected List<RefreshablePropertySource> getRefreshablePropertySources() {
	//	return Collections.singletonList(propertySource);
		return Arrays.asList(propertySource, propertySource2);
	}

}
