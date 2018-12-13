package io.github.pleuvoir.tookit.configuration.dynamic;

import java.util.Map;

/**
 * 动态配置接口，提供了动态读取 properties 文件的能力
 * @author pleuvoir
 *
 */
public interface DynamicConfig {

	void addListener(DynamicConfigListener listener);

	String getString(String key);

	String getString(String key, String defaultValue);

	int getInt(String key);

	int getInt(String key, int defaultValue);

	long getLong(String key);

	long getLong(String key, long defaultValue);

	double getDouble(String key);

	double getDouble(String key, double defaultValue);

	boolean getBoolean(String key, boolean defaultValue);

	boolean exist(String key);

	Map<String, String> toMap();
}