package io.github.pleuvoir.hikaricp.bestpractice;

public class Bootstrap2 extends AppConfig2 {

	public Bootstrap2(AppConfig2 appConfig) {
		appConfig.copyStateTo(this);
	}
}
