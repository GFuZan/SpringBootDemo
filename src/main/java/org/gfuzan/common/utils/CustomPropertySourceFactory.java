package org.gfuzan.common.utils;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

/**
 * @PropertySource 注解加载yml配置
 * @author GFuZan
 */
public class CustomPropertySourceFactory extends DefaultPropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		// 获取配置文件名
		String sourceName = name != null ? name : resource.getResource().getFilename();

		if (resource.getResource().exists() && (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml"))) {
			Properties propertiesFromYaml = loadYml(resource);
			return new PropertiesPropertySource(sourceName, propertiesFromYaml);
		} else {
			return super.createPropertySource(name, resource);
		}
	}

	private Properties loadYml(EncodedResource resource) throws IOException {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		factory.setResources(resource.getResource());
		factory.afterPropertiesSet();
		return factory.getObject();
	}
}