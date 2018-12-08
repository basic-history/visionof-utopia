package io.github.pleuvoir.hikaricp.bestpractice;

public class Bootstrap extends AppConfig {

	public Bootstrap(AppConfig appConfig) {
		appConfig.copyStateTo(this);
		
		// 如此，appConfig 中属性值已经可以直接 getXXX 了，否则还得 appConfig.getXXX，当属性多时有特效
	}
}
