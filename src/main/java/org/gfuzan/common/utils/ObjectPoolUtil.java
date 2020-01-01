package org.gfuzan.common.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 对象池工具
 * 
 * @author GFuZan
 *
 */
public class ObjectPoolUtil {

	/**
	 * 执行
	 * 
	 * @return
	 */
	public static <T, R> R excute(ObjectPool<T> objectPool, Function<T, R> function) {
		Objects.requireNonNull(function);

		T object = null;
		R apply = null;
		try {
			object = objectPool.borrowObject();
			apply = function.apply(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (object != null) {
				try {
					objectPool.returnObject(object);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return apply;
	}

	/**
	 * 获取对象池
	 * 
	 * @param <T>           对象类型
	 * @param objectFactory 对象创建工厂
	 * @return 对象池
	 */
	public static <T> ObjectPool<T> getObjectPool(Supplier<T> objectFactory) {
		return getObjectPool(objectFactory, t -> true, t -> {
		}, null);
	}

	/**
	 * 获取对象池
	 * 
	 * @param <T>            对象类型
	 * @param objectFactory  对象创建工厂
	 * @param destroyFactory 对象销毁工厂
	 * @return 对象池
	 */
	public static <T> ObjectPool<T> getObjectPool(Supplier<T> objectFactory, Consumer<T> destroyFactory) {
		return getObjectPool(objectFactory, t -> true, destroyFactory, null);
	}

	/**
	 * 获取对象池
	 * 
	 * @param <T>             对象类型
	 * @param objectFactory   对象创建工厂
	 * @param validateFactory 对象验证工厂
	 * @return 对象池
	 */
	public static <T> ObjectPool<T> getObjectPool(Supplier<T> objectFactory, Predicate<T> validateFactory) {
		return getObjectPool(objectFactory, validateFactory, t -> {
		}, null);
	}

	/**
	 * 获取对象池
	 * 
	 * @param <T>             对象类型
	 * @param objectFactory   对象创建工厂
	 * @param validateFactory 对象验证工厂
	 * @param destroyFactory  对象销毁工厂
	 * @return 对象池
	 */
	public static <T> ObjectPool<T> getObjectPool(Supplier<T> objectFactory, Predicate<T> validateFactory,
			Consumer<T> destroyFactory) {
		return getObjectPool(objectFactory, validateFactory, destroyFactory, null);
	}

	/**
	 * 获取对象池
	 * 
	 * @param <T>           对象类型
	 * @param objectFactory 对象创建工厂
	 * @param config        对象池配置
	 * @return 对象池
	 */
	public static <T> ObjectPool<T> getObjectPool(Supplier<T> objectFactory, GenericObjectPoolConfig<T> config) {
		return getObjectPool(objectFactory, t -> true, t -> {
		}, config);
	}

	/**
	 * 获取对象池
	 * 
	 * @param <T>            对象类型
	 * @param objectFactory  对象创建工厂
	 * @param destroyFactory 对象销毁工厂
	 * @param config         对象池配置
	 * @return 对象池
	 */
	public static <T> ObjectPool<T> getObjectPool(Supplier<T> objectFactory, Consumer<T> destroyFactory,
			GenericObjectPoolConfig<T> config) {
		return getObjectPool(objectFactory, t -> true, destroyFactory, config);
	}

	/**
	 * 获取对象池
	 * 
	 * @param <T>             对象类型
	 * @param objectFactory   对象创建工厂
	 * @param validateFactory 对象验证工厂
	 * @param config          对象池配置
	 * @return 对象池
	 */
	public static <T> ObjectPool<T> getObjectPool(Supplier<T> objectFactory, Predicate<T> validateFactory,
			GenericObjectPoolConfig<T> config) {
		return getObjectPool(objectFactory, validateFactory, t -> {
		}, config);
	}

	/**
	 * 获取对象池
	 * 
	 * @param <T>             对象类型
	 * @param objectFactory   对象创建工厂
	 * @param validateFactory 对象验证工厂
	 * @param destroyFactory  对象销毁工厂
	 * @param config          对象池配置
	 * @return 对象池
	 */
	public static <T> ObjectPool<T> getObjectPool(Supplier<T> objectFactory, Predicate<T> validateFactory,
			Consumer<T> destroyFactory, GenericObjectPoolConfig<T> config) {

		// 对象池工厂
		GenericObjectPoolFactory<T> factory = new GenericObjectPoolFactory<>(objectFactory, validateFactory,
				destroyFactory);

		if (Objects.isNull(config)) {
			config = new GenericObjectPoolConfig<>();
			// 最大空闲数
			config.setMaxIdle(2);
			// 对象池最大无限制
			config.setMaxTotal(-1);
			// 最大等待时间200ms
			config.setMaxWaitMillis(200);
			// 对象逐出连接的最小空闲时间
			config.setMinEvictableIdleTimeMillis(1000 * 60 * 30);
			// 最小空闲数量
			config.setMinIdle(1);
			// 连接耗尽时是否阻塞
			config.setBlockWhenExhausted(true);
		}

		return new GenericObjectPool<>(factory, config);
	}

	/**
	 * 对象池
	 * 
	 * @param <T> 对象类型
	 */
	private static class GenericObjectPoolFactory<T> extends BasePooledObjectFactory<T> {

		/**
		 * 对象工厂
		 */
		private final Supplier<T> objectFactory;

		/**
		 * 销毁工厂
		 */
		private final Consumer<T> destroyFactory;

		/**
		 * 验证工厂
		 */
		private final Predicate<T> validateFactory;

		/**
		 * 
		 * @param objectFactory   对象创建工厂
		 * @param validateFactory 对象验证工厂
		 * @param destroyFactory  对象销毁工厂
		 */
		private GenericObjectPoolFactory(Supplier<T> objectFactory, Predicate<T> validateFactory,
				Consumer<T> destroyFactory) {
			Objects.requireNonNull(objectFactory, "objectFactory 不能为空");
			Objects.requireNonNull(destroyFactory, "destroyFactory 不能为空");
			Objects.requireNonNull(validateFactory, "validateFactory 不能为空");

			this.objectFactory = objectFactory;
			this.validateFactory = validateFactory;
			this.destroyFactory = destroyFactory;
		}

		@Override
		public T create() throws Exception {
			return objectFactory.get();
		}

		@Override
		public PooledObject<T> wrap(T obj) {
			return new DefaultPooledObject<>(obj);
		}

		@Override
		public void destroyObject(final PooledObject<T> p) throws Exception {
			destroyFactory.accept(p.getObject());
		}

		@Override
		public boolean validateObject(PooledObject<T> p) {
			return validateFactory.test(p.getObject());
		}
	}
}
