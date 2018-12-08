package io.github.pleuvoir.hikaricp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import com.alibaba.fastjson.JSON;

public class HikariConfig {

	public HikariConfig() {
		super();
	}

	public HikariConfig(String propertyFileName) {
		this.loadProperties(propertyFileName);
	}

	private long initializationFailTimeout;
	private String driverClassName;
	private boolean isAutoCommit;
	private int age;
	private double salary;

	private void loadProperties(String propertyFileName) {
		final File propFile = new File(propertyFileName);
		try (final InputStream is = propFile.isFile() ? new FileInputStream(propFile) : this.getClass().getResourceAsStream(propertyFileName)) {
			if (is != null) {
				Properties props = new Properties();
				props.load(is);
				// 打印内容
				System.out.println(JSON.toJSON(props));
				PropertyElf.setTargetFromProperties(this, props);
			} else {
				throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
			}
		} catch (IOException io) {
			throw new RuntimeException("Failed to read property file", io);
		}
	}

	public void copyStateTo(HikariConfig other) {
		for (Field field : HikariConfig.class.getDeclaredFields()) {
			if (!Modifier.isFinal(field.getModifiers())) {
				field.setAccessible(true);
				try {
					field.set(other, field.get(this));
				} catch (Exception e) {
					throw new RuntimeException("Failed to copy HikariConfig state: " + e.getMessage(), e);
				}
			}
		}
	}

	
	// getter and setter
	
	public long getInitializationFailTimeout() {
		return initializationFailTimeout;
	}

	public void setInitializationFailTimeout(long initializationFailTimeout) {
		this.initializationFailTimeout = initializationFailTimeout;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}


}
