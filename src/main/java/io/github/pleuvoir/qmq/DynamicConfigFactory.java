package io.github.pleuvoir.qmq;

public interface DynamicConfigFactory {
	DynamicConfig create(String name, boolean failOnNotExist);
}