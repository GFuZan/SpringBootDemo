package org.gfuzan.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.gfuzan.RunApplication;
import org.gfuzan.modules.entity.User;
import org.gfuzan.modules.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RunApplication.class },webEnvironment=WebEnvironment.NONE)
public class DemoApplicationTests {

	@Autowired
	private UserService us;

	/**
	 * 多数据源测试
	 */
	@Test
	public void Test01() {
		// 数据源
		us.getAllUser();
		// 数据源1
		us.getAllUser1();
		// 数据源2
		us.getAllUser2();
		
		// 内部调用
		us.getAllUserAll();
		
		System.out.println();
	}
	
	/**
	 * mybatis 缓存测试
	 */
	@Test
	public void Test0101() {
		// 数据源2
		us.getAllUser2();
		// 数据源2
		us.getAllUser2();
		// 数据源2
		us.getAllUser2();
		// 数据源2
		us.getAllUser2();
		// 数据源2
		us.getAllUser2();
		System.out.println();
	}
	
	/**
	 * 事务测试
	 */
	@Test
	public void Test011() {
		// 数据源
		try {
			us.updateUserT();
		}catch (Exception e) {
		}
		// 数据源1
		try {
			us.updateUserT1();
		}catch (Exception e) {
		}
		// 数据源2
		try {
			us.updateUserT2();
		}catch (Exception e) {
		}
		
		System.out.println();
	}
	
	
	/**
	 * 分页测试
	 */
	@Test
	public void Test012() {
		List<User> allUserPage = us.getAllUserPage(-1);
		
		allUserPage = us.getAllUserPage(1);
		
		allUserPage = us.getAllUserPage(2);
		
		allUserPage = us.getAllUserPage(100);
		
		System.out.println(allUserPage);
	}

	@Autowired
	RedisTemplate<Object,Object> rt;
	
	@Autowired
	StringRedisTemplate srt;

	/**
	 * Redis测试
	 */
	@Test
	public void redisTest() {
		Boolean delete = rt.delete("testKey");
		System.out.println("删除成功: "+ delete);
		
		final String testSetKey = "testSetKey";
		
		final String testHashKey = "testHashKey";
		
		final String testListKey = "testListKey";
		
		final String testValueKey = "testValueKey";
		
		
		// set测试
		{
			SetOperations opsForSet = rt.opsForSet();
			// 添加元素
			Long add = opsForSet.add(testSetKey, new User("张三"),new User("李四"),new User("王五"));
			//设置有效期
			rt.expire(testSetKey, 30, TimeUnit.MINUTES);
			// 随机读取一个
			User randomMember = (User) opsForSet.randomMember(testSetKey);
			// 读取所有
			List<User> randomMembers = opsForSet.randomMembers(testSetKey, opsForSet.size(testSetKey));
		}
		
		
	}

	@Autowired
	private MessageSource messageSource;

	/**
	 * 多语言测试
	 */
	@Test
	public void Test02() {
		System.out.println(messageSource.getMessage("welcome", null, Locale.CHINA));
		System.out.println(messageSource.getMessage("welcome", null, Locale.ENGLISH));
	}

	/**
	 * JSON 与 Object 互转
	 * 
	 * @throws IOException
	 */
	@Test
	public void Test03() throws IOException {
		ObjectMapper om = new ObjectMapper();
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			userList.add(new User("张三" + i));
		}

		// toJSON
		String jsonObject = om.writeValueAsString(userList.get(0));
		System.err.println(jsonObject);

		// toJSON
		String jsonObjectList = om.writeValueAsString(userList);
		System.err.println(jsonObjectList);

		// toObject
		System.err.println(om.readValue(jsonObject, User.class));

		// toObjectList 方法1(Map同理)
		JavaType jt = om.getTypeFactory().constructParametricType(ArrayList.class, User.class);
		List<User> value = om.readValue(jsonObjectList, jt);
		System.err.println(value);

		// toObjectList 方法2(Map同理)
		value = om.readValue(jsonObjectList, new TypeReference<ArrayList<User>>() {
		});
		System.err.println(value);

	}

}
