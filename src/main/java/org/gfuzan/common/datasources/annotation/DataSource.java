package org.gfuzan.common.datasources.annotation;

import java.lang.annotation.*;

import org.gfuzan.common.datasources.DataSourceName;

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
