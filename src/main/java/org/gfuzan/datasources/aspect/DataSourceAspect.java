package org.gfuzan.datasources.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gfuzan.datasources.DataSourceName;
import org.gfuzan.datasources.DynamicDataSource;
import org.gfuzan.datasources.annotation.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 多数据源，切面处理类
 * 
 * @author gaofz
 *
 */
@Aspect
@Order(-1)
@Component
public class DataSourceAspect {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String dataSourceAnnotationClassName = "org.gfuzan.datasources.annotation.DataSource";

	@Pointcut("@annotation("+dataSourceAnnotationClassName+")")
	public void dataSourcePointCut() {
	}

	@Around("dataSourcePointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();

		DataSource ds = method.getAnnotation(DataSource.class);

		DataSourceName dname = ds.value();
		DynamicDataSource.setDataSource(dname.getName());
		logger.debug("设置数据源: " + dname.getName());

		try {
			return point.proceed();
		} finally {
			String dataSourceName = DynamicDataSource.clearDataSource();
			logger.debug("清理数据源: " + dataSourceName);
		}
	}
}
