package io.github.pleuvoir.qmq.bestpractice;

import java.util.concurrent.TimeUnit;

public class Client {

	public static void main(String[] args) throws InterruptedException {
		
//		ReloadPropertiesable adapter = new ReloadPropertiesable.Adapter("config/appconfig.properties");
//		System.out.println(adapter.getString("app.name"));
//		
		AppConfig3 appConfig3 = new AppConfig3("config/appconfig.properties");
		
		while(true) {
			TimeUnit.SECONDS.sleep(1);
			System.out.println(appConfig3.getString("app.name"));
		}
	}
}
