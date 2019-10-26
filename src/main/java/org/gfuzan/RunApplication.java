package org.gfuzan;

import org.gfuzan.common.utils.CustomPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableCaching
@EnableTransactionManagement
@EnableScheduling
@PropertySource(value = { "${location.datasource:}","${location.cache:}","${location.cors:}" }, factory = CustomPropertySourceFactory.class, ignoreResourceNotFound = true)
public class RunApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RunApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RunApplication.class);
	}
}
