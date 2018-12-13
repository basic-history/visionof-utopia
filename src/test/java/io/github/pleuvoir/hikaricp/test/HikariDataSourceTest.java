package io.github.pleuvoir.hikaricp.test;

import com.alibaba.fastjson.JSON;

import io.github.pleuvoir.hikaricp.HikariConfig;
import io.github.pleuvoir.hikaricp.HikariDataSource;
import io.github.pleuvoir.tookit.configuration.object.PropLoaderUtil;

/**
 * 测试 #{HikariDataSource} 类
 * @author pleuvoir
 *
 */
public class HikariDataSourceTest {

	public static void main(String[] args) {

		HikariConfig config = PropLoaderUtil.setTargetFromProperties(new HikariConfig(), "/HikariConfigTest.properties");
		HikariDataSource hikariDataSource = new HikariDataSource(config);
		// 属性值都是空的
		hikariDataSource.dosomething1();
		
		HikariDataSource hikariDataSource2 = new HikariDataSource(config);

		System.out.println(JSON.toJSON(hikariDataSource2));

		hikariDataSource2.dosomething2();
	}
}
