package org.gfuzan.common.config.datasources.annotation;

import java.lang.annotation.*;

import org.gfuzan.common.config.datasources.DataSourceName;

/**
 * 多数据源注解
 * @author GFuZan
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
	DataSourceName value();
}
