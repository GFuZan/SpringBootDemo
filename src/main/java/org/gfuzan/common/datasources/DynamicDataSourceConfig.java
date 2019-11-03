package org.gfuzan.common.datasources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

/**
 * 配置多数据源
 * 
 * @author GFuZan
 *
 */
@Configuration
public class DynamicDataSourceConfig {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 创建数据源配置bean
	 * 
	 * @return
	 */
	@Bean("dataSourceMap")
	@ConfigurationProperties(prefix = "spring.datasource", ignoreInvalidFields = true)
	public Map<String, HikariDataSource> createDataSourceConfig() {
		return new HashMap<>();
	}

	@Bean
	public DataSource dataSource(Map<String, ? extends DataSource> dataSourceMap) {

		if(!dataSourceMap.isEmpty()) {
			Set<String> keySet = dataSourceMap.keySet();
			keySet.forEach((key)->{
				logger.debug("找到数据源配置: "+ key);
			});
		}
		
		// 找到默认数据源
		DataSource defaultDataSource = dataSourceMap.get(DataSourceName.getDefaultDataSourceName().getName());

		return new DynamicDataSource(defaultDataSource, dataSourceMap);
	}

//	/**
//	 * 创建数据源配置bean
//	 * 
//	 * @return
//	 */
//	@Bean("dataSourceMap")
//	@ConfigurationProperties(prefix="spring.datasource",ignoreInvalidFields=true)
//	public Map<String, Map<String,Object>> createDataSourceConfig() {
//		return new HashMap<>();
//	}
//
//	@Bean
//	public DynamicDataSource dataSource(Map<String, Map<String,Object>> dataSourceMap) {
//		
//		Map<String,DataSource> dataSources = new ConcurrentHashMap<>();
//		
//
//		if(!dataSourceMap.isEmpty()) {
//			Set<Entry<String,Map<String,Object>>> entrySet = dataSourceMap.entrySet();
//			
//			entrySet.forEach((e)->{
//				String key = null;
//				Object value = null;
//				if(e != null && (key = e.getKey()) != null && (value = e.getValue()) != null) {
//					// Bean包装类,用与读取配置文件
//					BeanWrapper bw = new BeanWrapperImpl(DataSourceBuilder.create().build());
//					
//					Map<?,?> prop = null;
//					if(value instanceof Map) {
//						prop = (Map<?,?>)value;
//					}else if(!(value instanceof String)){
//						prop = BeanMap.create(value);
//					}
//
//					if(prop != null) {
//						// 设置配置
//						bw.setPropertyValues(new MutablePropertyValues(prop),true,false);
//						// 获取Bean
//						dataSources.put(key, (DataSource)bw.getWrappedInstance());
//					}
//				}
//			});
//		}
//
//		// 找到默认数据源
//		DataSource defaultDataSource = dataSources.get(DataSourceName.getDefaultDataSourceName().getName());
//
//		return new DynamicDataSource(defaultDataSource, dataSources);
//	}
}
