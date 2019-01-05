package io.github.pleuvoir.apollo.bestpractice;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.github.pleuvoir.apollo.spring.AppConfig;

@SuppressWarnings("resource")
public class Client {

	
	public static void main(String[] args) throws IOException {


		AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);
		for (String beanDefinitionName : app.getBeanDefinitionNames()) {
			System.out.println(beanDefinitionName);
		}

		// 定时监测线程，默认是 10 秒监测一次，可以改为动态配置的
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "check-and-test");
			t.setDaemon(true);
			return t;
		});

		executorService.scheduleWithFixedDelay(() -> {
			try {
				System.out.println("获取值mockKey：         " + app.getEnvironment().getProperty("mockKey"));
				System.out.println("获取值mockKey2：      " + app.getEnvironment().getProperty("mockKey2"));
			} catch (Throwable t) {
				t.getStackTrace();
			}
		}, 0L, 1L, TimeUnit.SECONDS);

		
		System.in.read();
		app.close();
	}

}
