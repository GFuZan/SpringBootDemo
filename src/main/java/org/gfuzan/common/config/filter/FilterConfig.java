package org.gfuzan.common.config.filter;

import org.gfuzan.common.config.filter.requestwrapper.RequestWrapperFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class FilterConfig {

	@Bean("requestWrapperFilter")
	public FilterRegistrationBean<RequestWrapperFilter> registFilter() {
		FilterRegistrationBean<RequestWrapperFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new RequestWrapperFilter());
		registration.addUrlPatterns("/*");
		registration.setName("RequestWrapperFilter");
		registration.setOrder(-999);
		return registration;
	}
	
	
}
