package org.gfuzan.demo;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.gfuzan.RunApplication;
import org.gfuzan.common.utils.ObjectPoolUtil;
import org.gfuzan.modules.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author GFuZan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RunApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ObjectPoolUtilTest {

	private static GenericObjectPoolConfig<User> config = new GenericObjectPoolConfig<>();
	static {
		// 最大空闲数
		config.setMaxIdle(2);
		// 对象池最大无限制
		config.setMaxTotal(-1);
		// 最大等待时间200ms
		config.setMaxWaitMillis(200);
		// 对象逐出连接的最小空闲时间,设置为1秒方便测试
		config.setMinEvictableIdleTimeMillis(1000);
		// 最小空闲数量
		config.setMinIdle(1);
		// 连接耗尽时是否阻塞
		config.setBlockWhenExhausted(true);
	}

	private final ObjectPool<User> objectPool = ObjectPoolUtil.getObjectPool(() -> {
		User user = new User();
		Random random = new Random();
		user.setId(random.nextInt(2000));
		user.setAge(0);
		System.out.printf("--------创建对象 user.id=%5d\n", user.getId());
		return user;
	}, (e) -> {
		System.out.printf("--------验证对象是否可用 user.id=%5d\n", e.getId());
		return e.getId() != null;
	}, (e) -> {
		System.out.printf("--------销毁对象 user.id=%5d\n", e.getId());
	}, config);

	/**
	 * 基础使用
	 */
	@Test
	public void test_01() {

		User user = null;
		try {
			user = objectPool.borrowObject();
			user.setName(Thread.currentThread().getName());
			user.setAge(user.getAge() + 1);
			System.out.printf(
					"objectPool.size = %3d, objectPool.NumActive = %3d, objectPool.NumIdle = %3d, user.id = %5d, user.name = %s, user.age = %s\n",
					objectPool.getNumActive() + objectPool.getNumIdle(), objectPool.getNumActive(),
					objectPool.getNumIdle(), user.getId(), user.getName(), user.getAge());

			// 模拟执行
			Thread.sleep(new Random().nextInt(100) + 130);
		} catch (Exception e) {
			System.out.println("获取对象失败");
			e.printStackTrace();
		} finally {
			if (user != null) {
				try {
					objectPool.returnObject(user);
				} catch (Exception e) {
					System.out.println("返还对象失败");
					e.printStackTrace();
				}
			}
		}
	}

		/**
	 * 执行器测试
	 */
	@Test
	public void test_02() {
		ObjectPoolUtil.excute(objectPool, (user) -> {
			user.setName(Thread.currentThread().getName());
			user.setAge(user.getAge() + 1);
			System.out.printf(
					"objectPool.size = %3d, objectPool.NumActive = %3d, objectPool.NumIdle = %3d, user.id = %5d, user.name = %s, user.age = %s\n",
					objectPool.getNumActive() + objectPool.getNumIdle(), objectPool.getNumActive(),
					objectPool.getNumIdle(), user.getId(), user.getName(), user.getAge());

			// 模拟执行
			try {
				Thread.sleep(new Random().nextInt(100) + 130);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return true;
		});
	}

	/**
	 * 多线程测试
	 */
	@Test
	public void test_03() {
		ExecutorService threadPool = Executors.newFixedThreadPool(50);

		Runnable run = () -> {
			while (true) {
				test_02();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		for (int i = 0; i < 4; i++) {
			threadPool.execute(run);
		}

		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
