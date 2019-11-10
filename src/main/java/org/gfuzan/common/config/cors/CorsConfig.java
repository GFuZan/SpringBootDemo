package org.gfuzan.common.config.cors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 * 
 * @author GFuZan
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnMissingBean(WebMvcConfigurer.class)
public class CorsConfig {
	
	private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

	@Bean
	public WebMvcConfigurer corsConfigurer(Map<String, String> corsMappingConfig,Map<String, List<String>> corsAllowRuleConfig) {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				if (!corsMappingConfig.isEmpty() && !corsAllowRuleConfig.isEmpty()) {
					Set<Entry<String, String>> entrySet = corsMappingConfig.entrySet();
					entrySet.forEach((map) -> {
						if (map != null && map.getValue() != null) {
							
							List<String> corsAllowRuleList = corsAllowRuleConfig.get(map.getKey());
							
							if(corsAllowRuleList != null) {
								corsAllowRuleList.forEach((e) -> {
									registry.addMapping(map.getValue()).allowedOrigins(e);
									log.debug("跨域规则:  "+ e +" -> "+ map.getValue());
								});
							}
							
						}
					});
				}
			}
		};
	}

	@Bean("corsMappingConfig")
	@ConfigurationProperties(prefix = "cors.allow-rule.mapping", ignoreUnknownFields = true)
	Map<String, String> createCorsMappingConfig() {
		return new HashMap<>();
	}
	
	@Bean("corsAllowRuleConfig")
	@ConfigurationProperties(prefix = "cors.allow-rule.allowed-origins", ignoreUnknownFields = true)
	Map<String, List<String>> createCorsAllowRuleConfig() {
		return new HashMap<>();
	}
}
