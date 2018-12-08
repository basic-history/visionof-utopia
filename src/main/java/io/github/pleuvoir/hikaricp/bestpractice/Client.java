package io.github.pleuvoir.hikaricp.bestpractice;

import com.alibaba.fastjson.JSON;

public class Client {

	public static void main(String[] args) {
		// 直接加载为对象
		Bootstrap bootstrap = new Bootstrap(AppConfig.load("/config/appconfig.properties"));
		System.out.println(JSON.toJSON(bootstrap));
	}
}
