package io.github.pleuvoir.configuration.dynamic.test;

import java.util.concurrent.TimeUnit;

import io.github.pleuvoir.tookit.configuration.dynamic.DynamicConfig;
import io.github.pleuvoir.tookit.configuration.dynamic.DynamicConfigListener;
import io.github.pleuvoir.tookit.configuration.dynamic.DynamicConfigLoader;

public class DynamicConfigLoaderTest {

	public static void main(String[] args) throws InterruptedException {
		DynamicConfig config = DynamicConfigLoader.load("config/appconfig.properties");
		
		config.addListener(new DynamicConfigListener() {
			@Override
			public void onLoad(DynamicConfig config) {
				System.out.println("我被重新载入了。。");
			}
		});
		config.addListener(new DynamicConfigListener() {
			@Override
			public void onLoad(DynamicConfig config) {
				System.out.println("我被重新载入了22。。");
			}
		});
		
		// 直接返回，不会再次读取
		DynamicConfigLoader.load("config/appconfig.properties");
		
		while(true) {
			TimeUnit.SECONDS.sleep(1);
			System.out.println(config.getString("app.name"));
		}
	}
	
}
