package org.gfuzan.datasources;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

/**
 * 配置多数据源
 * 
 * @author GFuZan
 *
 */
@Configuration
public class DynamicDataSourceConfig {

	/**
	 * 创建数据源配置bean
	 * 
	 * @return
	 */
	@Bean("dataSourceMap")
	@ConfigurationProperties(prefix="spring.datasource",ignoreInvalidFields=true)
	public Map<String, HikariDataSource> createDataSourceConfig() {
		return new HashMap<>();
	}

	@Bean
	@Primary
	public DynamicDataSource dataSource(Map<String,? extends DataSource> dataSourceMap) {

		// 找到默认数据源
		DataSource defaultDataSource = dataSourceMap.get(DataSourceName.getDefaultDataSourceName().getName());

		return new DynamicDataSource(defaultDataSource, dataSourceMap);
	}
}
