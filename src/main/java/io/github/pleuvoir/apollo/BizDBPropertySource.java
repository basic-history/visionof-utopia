package io.github.pleuvoir.apollo;

import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
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