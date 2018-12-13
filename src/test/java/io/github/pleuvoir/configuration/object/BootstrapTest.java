package io.github.pleuvoir.configuration.object;

public class BootstrapTest {

	public static void main(String[] args) throws InterruptedException {
		
		AppConfig appConfig = new AppConfig();
		appConfig.load("config/appconfig.properties","app.");
		System.out.println(appConfig);
		
		Bootstrap bootstrap = new Bootstrap(appConfig);
		System.out.println(bootstrap);
	}
	
}
