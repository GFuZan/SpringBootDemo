package org.gfuzan.common.config.datasources.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gfuzan.common.config.datasources.DataSourceName;
import org.gfuzan.common.config.datasources.DynamicDataSource;
import org.gfuzan.common.config.datasources.annotation.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 多数据源，切面处理类
 * 
 * @author GFuZan
 *
 */
@Aspect
// 保证在事务处理之前切换数据源
@Order(-1)
@Component
public class DataSourceAspect {

	private static final Logger log = LoggerFactory.getLogger(DataSourceAspect.class);

	private static final String dataSourceAnnotationClassName = "org.gfuzan.common.config.datasources.annotation.DataSource";

	@Pointcut("@annotation(" + dataSourceAnnotationClassName + ")")
	public void dataSourcePointCut() {
	}

	@Around("dataSourcePointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();

		DataSource ds = method.getAnnotation(DataSource.class);

		DataSourceName dname = ds.value();
		DynamicDataSource.setDataSource(dname.getName());
		log.debug("设置数据源: " + dname.getName());

		try {
			return point.proceed();
		} finally {
			String dataSourceName = DynamicDataSource.clearDataSource();
			log.debug("清理数据源: " + dataSourceName);
		}
	}
}
