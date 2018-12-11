package io.github.pleuvoir.qmq.test;

import java.util.concurrent.TimeUnit;

import io.github.pleuvoir.qmq.DynamicConfig;
import io.github.pleuvoir.qmq.DynamicConfigLoader;
import io.github.pleuvoir.qmq.Listener;

public class DynamicConfigLoaderTest {

	public static void main(String[] args) throws InterruptedException {
		DynamicConfig config = DynamicConfigLoader.load("config/appconfig.properties");
		config.addListener(new Listener() {
			
			@Override
			public void onLoad(DynamicConfig config) {
				System.out.println("我被重新载入了。。");
			}
		});
		
		while(true) {
			TimeUnit.SECONDS.sleep(1);
			System.out.println(config.getString("app.name"));
		}
	}
}
