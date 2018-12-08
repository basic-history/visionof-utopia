package io.github.pleuvoir.hikaricp.test;

import java.util.Arrays;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import io.github.pleuvoir.hikaricp.HikariConfig;
import io.github.pleuvoir.hikaricp.PropertyElf;
import io.github.pleuvoir.tookit.PropLoaderUtil;

/**
 * 测试 #{PropertyElf} 类
 * @author pleuvoir
 *
 */
public class PropertyElfTest {

	public static void main(String[] args) {
	    
		// 读取类的 所有 field 
		Set<String> propertyNames = PropertyElf.getPropertyNames(HikariConfig.class);
		// 这里 boolean 类型的会有问题
		System.out.println("HikariConfig 所有字段： " + Arrays.asList(propertyNames));
				
		// 使用我的黑魔法获取 properties
	//	HikariConfig hikariConfig = new HikariConfig("/HikariConfigTest.properties");
		HikariConfig hikariConfig = new HikariConfig();
		PropertyElf.setTargetFromProperties(hikariConfig, PropLoaderUtil.loadProperties("/HikariConfigTest.properties"));
		System.out.println(JSON.toJSON(hikariConfig));
		
		
		// 使用我的黑魔法类
		HikariConfig myHikariConfig = new HikariConfig();
		PropLoaderUtil.setTargetFromProperties(myHikariConfig, PropLoaderUtil.loadProperties("/HikariConfigTest.properties"));
		System.out.println(JSON.toJSON(myHikariConfig));
		myHikariConfig = new HikariConfig();
		PropLoaderUtil.setTargetFromProperties(myHikariConfig, "/HikariConfigTest.properties");
		System.out.println(JSON.toJSON(myHikariConfig));
		myHikariConfig = new HikariConfig();
		
		// 忽略前缀
		PropLoaderUtil.setTargetFromProperties(myHikariConfig, "/HikariConfigTest.properties", "spring.");
		System.out.println(JSON.toJSON(myHikariConfig));
	}
}
