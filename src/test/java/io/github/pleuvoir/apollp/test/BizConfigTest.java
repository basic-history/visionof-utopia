package io.github.pleuvoir.apollp.test;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.github.pleuvoir.apollo.spring.AppConfig;

public class BizConfigTest {
	
	public static void main(String[] args) throws IOException {
		
		AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);
		app.getEnvironment().getPropertySources().forEach(k ->{
			System.out.println(k.getName());
		});
		// systemProperties
		// systemEnvironment
		// DBConfig

		System.in.read();
		app.close();
	}
	
	
}
