package org.gfuzan.datasources.annotation;

import java.lang.annotation.*;

import org.gfuzan.datasources.DataSourceName;

/**
 * 多数据源注解
 * @author gaofz
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
	DataSourceName value();
}
