package io.github.pleuvoir.tookit.configuration.object;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class CopyStateUtil {

	/**
	 * 拷贝对象属性至新对象，类似于 Beanutils.copy()，属于浅拷贝
	 * @param source 原目标
	 * @param target 目标对象
	 */
	public static void copyStateTo(Object source, Object target) {
		for (Field field : source.getClass().getDeclaredFields()) {
			if (!Modifier.isFinal(field.getModifiers())) {
				field.setAccessible(true);
				try {
					field.set(target, field.get(source));
				} catch (Exception e) {
					throw new RuntimeException("Failed to copy state: " + e.getMessage(), e);
				}
			}
		}
	}

}
