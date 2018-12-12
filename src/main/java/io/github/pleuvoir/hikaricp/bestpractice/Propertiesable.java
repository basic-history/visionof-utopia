package io.github.pleuvoir.hikaricp.bestpractice;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import io.github.pleuvoir.tookit.PropLoaderUtil;

/**
 * 代表支持 properties 文件到对象的转换
 * @author pleuvoir
 *
 */
public interface Propertiesable {

	default void load(String propPath) {
		PropLoaderUtil.setTargetFromProperties(this, propPath);
	}

	default void load(String propPath, String ignorePrefix) {
		PropLoaderUtil.setTargetFromProperties(this, propPath, ignorePrefix);
	}

	default void copyStateTo(Object target) {
		for (Field field : getClass().getDeclaredFields()) {
			if (!Modifier.isFinal(field.getModifiers())) {
				field.setAccessible(true);
				try {
					field.set(target, field.get(this));
				} catch (Exception e) {
					throw new RuntimeException("Failed to copy state: " + e.getMessage(), e);
				}
			}
		}
	}
}
