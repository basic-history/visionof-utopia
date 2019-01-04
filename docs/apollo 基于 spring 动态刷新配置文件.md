
本文是对  apollo 中动态加载配置类的简要分析以及学习，此实现是和 `spring` 耦合的。可以方便的获取配置的值（配置存储可以自行实现，此例子中是从 database 获取），并且实现了实时刷新的功能。

## 设计概览

通过 扩展`spring` 中 `MapPropertySource` 来实现从更新 `Environment` 的功能。

### 接口及现有的实现

该类继承  `spring` 中 `MapPropertySource`，使得配置属性可以通过像从 `map` 中取值一样去读取

```java
public abstract class RefreshablePropertySource extends MapPropertySource {

// source 为 map
public RefreshablePropertySource(String name, Map<String, Object> source) {
	super(name, source);
}

@Override
public Object getProperty(String name) {
	return this.source.get(name);
}

/**

 * 留给子类的刷新方法，具体如何刷新由子类负责实现
 */
protected abstract void refresh();

}
```

子类实现，这个类会读取数据库配置并更新 source

```java
@Component
public class BizDBPropertySource extends RefreshablePropertySource {

	private static final Logger logger = LoggerFactory.getLogger(BizDBPropertySource.class);

	//@Autowired
	//private ServerConfigRepository serverConfigRepository;

	public BizDBPropertySource(String name, Map<String, Object> source) {
		super(name, source);
	}

	public BizDBPropertySource() {
		super("DBConfig", Maps.newConcurrentMap());
	}


	@Override
	protected void refresh() {
		
		// 从数据库获取数据并刷新到容器中，用户自行实现
	//	Iterable<ServerConfig> dbConfigs = serverConfigRepository.findAll();

		Map<String, Object> newConfigs = Maps.newHashMap();

		// data center's configs
//		String dataCenter = getCurrentDataCenter();
//		for (ServerConfig config : dbConfigs) {
//			if (Objects.equals(dataCenter, config.getCluster())) {
//				newConfigs.put(config.getKey(), config.getValue());
//			}
//		}
	

		// put to environment
		for (Map.Entry<String, Object> config : newConfigs.entrySet()) {
			String key = config.getKey();
			Object value = config.getValue();

			if (this.source.get(key) == null) {
				logger.info("Load config from DB : {} = {}", key, value);
			} else if (!Objects.equals(this.source.get(key), value)) {
				logger.info("Load config from DB : {} = {}. Old value = {}", key, value, this.source.get(key));
			}

			this.source.put(key, value);

		}

	}

}
```

刷新逻辑的实现，可以看到和 `qmq` 中刷新的套路一样，都是 `newSingleThreadScheduledExecutor`

```java
public abstract class RefreshableConfig {

	private static Logger logger = LoggerFactory.getLogger(RefreshableConfig.class);

	private static final String LIST_SEPARATOR = ",";
	// TimeUnit: second
	private static final int CONFIG_REFRESH_INTERVAL = 60;

	@Autowired
	private ConfigurableEnvironment environment;

	private List<RefreshablePropertySource> propertySources;

	/**
	 * register refreshable property source.
	 * Notice: The front property source has higher priority.
	 */
	protected abstract List<RefreshablePropertySource> getRefreshablePropertySources();

	@PostConstruct
	public void setup() {

		// 如何获取数据，留给子类去实现   并用线程不断的进行调用
		propertySources = getRefreshablePropertySources();
		if (CollectionUtils.isEmpty(propertySources)) {
			throw new IllegalStateException("Property sources can not be empty.");
		}

		// add property source to environment
		for (RefreshablePropertySource propertySource : propertySources) {
			propertySource.refresh();
			environment.getPropertySources().addLast(propertySource);
		}

		// task to update configs
		
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r, "refresh-config-watcher");
			t.setDaemon(true);
			return t;
		});
		

		executorService.scheduleWithFixedDelay(() -> {
			try {
				propertySources.forEach(RefreshablePropertySource::refresh);
			} catch (Throwable t) {
				logger.error("Refresh configs failed.", t);
			}
		}, CONFIG_REFRESH_INTERVAL, CONFIG_REFRESH_INTERVAL, TimeUnit.SECONDS);
	}

	public int getIntProperty(String key, int defaultValue) {
		try {
			String value = getValue(key);
			return value == null ? defaultValue : Integer.parseInt(value);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public boolean getBooleanProperty(String key, boolean defaultValue) {
		try {
			String value = getValue(key);
			return value == null ? defaultValue : "true".equals(value);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public String[] getArrayProperty(String key, String[] defaultValue) {
		try {
			String value = getValue(key);
			return Strings.isNullOrEmpty(value) ? defaultValue : value.split(LIST_SEPARATOR);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public String getValue(String key, String defaultValue) {
		try {
			return environment.getProperty(key, defaultValue);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public String getValue(String key) {
		return environment.getProperty(key);
	}

}
```

### 测试

```java
	AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(AppConfig.class);
	app.getEnvironment().getPropertySources().forEach(k ->{
		System.out.println(k.getName());
	});
	// systemProperties
	// systemEnvironment
	// DBConfig
```

可以看出，我们新增的 `DBConfig` 已经被加入到 `spring` 的  `Environment` 中。实现较为简单，可以和 `qmq` 的读取配置的实现结合起来，方便用 `spring` 的 `Environment` 方式获取，这个特性也不错。

