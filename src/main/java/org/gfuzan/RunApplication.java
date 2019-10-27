package org.gfuzan;

import java.net.URL;
import java.net.URLClassLoader;

import org.gfuzan.common.classloader.CustomClassLoader;
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
	
	
	/**
	 * 设置自定义类加载器
	 */
	static{
		CustomClassLoader customClassLoader = new CustomClassLoader();
		URLClassLoader classLoader = (URLClassLoader) RunApplication.class.getClassLoader();
		URL[] urLs = classLoader.getURLs();
		for(URL url : urLs) {
			customClassLoader.addURL(url);
		}
		Thread.currentThread().setContextClassLoader(customClassLoader);
	}
}
