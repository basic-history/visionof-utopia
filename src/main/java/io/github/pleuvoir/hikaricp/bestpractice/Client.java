package io.github.pleuvoir.hikaricp.bestpractice;

import com.alibaba.fastjson.JSON;

public class Client {

	public static void main(String[] args) {
		// 直接加载为对象
		//Bootstrap bootstrap = new Bootstrap(AppConfig.load("/config/appconfig.properties"));
	//	System.out.println(JSON.toJSON(bootstrap));
		
		// 实现 Propertiesable 接口
		AppConfig2 appConfig2 = new AppConfig2();
		appConfig2.load("/config/appconfig.properties", "app.");
		System.out.println(JSON.toJSON(appConfig2));
		
		Bootstrap2 bootstrap2 = new Bootstrap2(appConfig2);
		System.out.println(JSON.toJSON(bootstrap2));
		
	}
}
