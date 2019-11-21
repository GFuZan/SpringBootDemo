package org.gfuzan.common.utils;

import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
	private final static ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByExtension("js");

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
		scriptEngine.setContext(new SimpleScriptContext());
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
							msg.append(getObjectMapper().writeValueAsString(param));
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
		log.debug("设置 springContext");
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
     * 将对象转换为Json字符串
     * 
     * @param  obj
     * @return     json字串
     */
    public static String toJSON(Object obj) {
        try {
            String json = getObjectMapper().writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取对象
     * <p><b>用法示例:</b><br/>
     * <pre>
     *      <b>JSON 转为Map</b>
     *          Map< String,User > map =  getObject(json,new TypeReference< Map< String,User >>() {});
     * 
     *      <b>JSON 转为List</b>
     *          List< User > list = getObject(json,new TypeReference< List< User >>() {});
     * <pre>
     * </p>
     * @param json JSON字串
     * @param valueTypeRef 返回值类型
     * @return
     */
    public static <T> T getObject(String json,TypeReference<T> valueTypeRef) {
        try {
            return getObjectMapper().readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
	
	public static class KeyValue<K,V>{
		/**
		 * 键
		 */
		private K key;
		/**
		 * 值
		 */
		private V value;
		
		public K getKey() {
			return key;
		}
		
		public void setKey(K key) {
			this.key = key;
		}
		
		public V getValue() {
			return value;
		}
		
		public void setValue(V value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("rawtypes")
			KeyValue other = (KeyValue) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}
	}

	/**
	 * 对象包装类
	 * 
	 * @author GFuZan
	 *
	 * @param <T> 包装类型
	 */
	public static class ObjectWrapper<T> {
		/**
		 * 值
		 */
		private T value;
		/**
		 * 状态
		 */
		private int status;

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}
}
