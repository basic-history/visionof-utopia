package io.github.pleuvoir.tookit.configuration.dynamic;

public final class DynamicConfigLoader {

	private static final DynamicConfigFactory CONFIG_FACTORY;

	static {
		CONFIG_FACTORY = new DefaultDynamicConfigFactory();
	}

	private DynamicConfigLoader() {
	}

	/**
	 * 加载 properties 文件
	 * @param name	文件路径，可以是 config/config.properties 这样的格式
	 * @return 动态配置
	 */
	public static DynamicConfig load(final String name) {
		return load(name, true);
	}

	/**
	 * 加载 properties 文件
	 * @param name	文件路径，可以是 config/config.properties 这样的格式
	 * @param failOnNotExist	为 true 时未找到文件会抛出异常
	 * @return	动态配置
	 */
	public static DynamicConfig load(final String name, final boolean failOnNotExist) {
		return CONFIG_FACTORY.create(name, failOnNotExist);
	}
}