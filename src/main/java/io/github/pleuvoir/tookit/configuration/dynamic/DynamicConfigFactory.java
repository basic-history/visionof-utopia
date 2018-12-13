package io.github.pleuvoir.tookit.configuration.dynamic;

public interface DynamicConfigFactory {

	DynamicConfig create(String name, boolean failOnNotExist);

}