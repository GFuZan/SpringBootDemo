package org.gfuzan.common.utils;

import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 常用工具
 * 
 * @author GFuZan
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CommonUtil {

	/**
	 * Spring 上下文
	 */
	@Autowired
	private ApplicationContext springContext;

	/**
	 * 静态 Spting 上下文
	 */
	private static ApplicationContext sSpringContext;

	/**
	 * JSON 工具
	 */
	private static ObjectMapper om = getObjectMapper();

	/**
	 * 脚本执行器
	 */
	private static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByExtension("js");

	private final static Logger log = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * 算数表达式执行工具
	 * 
	 * @param expression 表达式
	 * @return 执行结果
	 */
	public static Object getExpressionResult(String expression) {
		return getExpressionResult(expression, false);
	}

	/**
	 * 算数表达式执行工具
	 * 
	 * @param expression     表达式
	 * @param throwException 当表达式执行出错时,是否抛出异常
	 * @return 执行结果
	 */
	public static Object getExpressionResult(String expression, boolean throwException) {
		Object res = null;
		try {
			res = scriptEngine.eval(expression);
		} catch (ScriptException e) {
			if (throwException) {
				throw new RuntimeException(e);
			} else {
				log.error("执行异常! 表达式: " + expression, e);
			}
		}

		return res;
	}

	/**
	 * 异常处理
	 * 
	 * @param func  执行方法
	 * @param param 参数
	 * @return null or 执行结果
	 */
	public static <T, R> R exceptionHandle(Function<T, R> func, T param) {
		return exceptionHandle(func, param, true);
	}

	/**
	 * 异常处理
	 * 
	 * @param func    执行方法
	 * @param param   参数
	 * @param showLog 执行出错时打印Log
	 * @return null or 执行结果
	 */
	public static <T, R> R exceptionHandle(Function<T, R> func, T param, boolean showLog) {
		R res = null;
		if (func != null) {
			try {
				res = func.apply(param);
			} catch (Exception e) {
				if (showLog) {
					StringBuilder msg = new StringBuilder();
					if (param != null) {
						msg.append("执行参数: ");
						try {
							msg.append(om.writeValueAsString(param));
						} catch (Exception e1) {
							msg.append(param);
						}
					}
					log.error(msg.toString(), e);
				}
			}
		}

		return res;
	}

	@PostConstruct
	private void init() {
		setSpringContext(springContext);
		try {
			setObjectMapper(springContext.getBean(ObjectMapper.class));
			log.debug("设置 ObjectMapper");
		} catch (Exception e) {
			log.warn("获取 ObjectMapper Bean 异常", e);
		}
	}

	/**
	 * 设置 ObjectMapper
	 * 
	 * @param objectMapper
	 */
	private synchronized static void setObjectMapper(ObjectMapper objectMapper) {
		om = objectMapper;
	}

	/**
	 * 获取 ObjectMapper
	 * 
	 * @return
	 */
	public static ObjectMapper getObjectMapper() {
		return om != null ? om : new ObjectMapper();
	}

	/**
	 * 设置 Spring 上下文
	 * 
	 * @param springContext
	 */
	private synchronized static void setSpringContext(ApplicationContext springContext) {
		sSpringContext = springContext;
	}

	/**
	 * 获取 Spring 上下文
	 * 
	 * @return
	 */
	public static ApplicationContext getSpringContext() {
		return sSpringContext;
	}
}
