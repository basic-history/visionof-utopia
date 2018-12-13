package io.github.pleuvoir.configuration.object;

public class Bootstrap extends AppConfig {

	public Bootstrap(AppConfig appConfig) {
		appConfig.copyStateTo(this);
	}
}
