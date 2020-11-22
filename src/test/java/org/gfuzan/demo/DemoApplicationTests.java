package org.gfuzan.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;

import org.gfuzan.RunApplication;
import org.gfuzan.common.utils.CommonUtil;
import org.gfuzan.modules.dto.UserVo;
import org.gfuzan.modules.entity.User;
import org.gfuzan.modules.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SimpleIdGenerator;
import org.springframework.util.SocketUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * @author GFuZan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RunApplication.class },webEnvironment=WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

	@Autowired
	private UserService us;

	/**
	 * 测试手动事务
	 */
	@Test
	public void testManualTransaction() {
		// 正常
		us.testManualTransaction("tableName");
		// 异常
		try {
			us.testManualTransaction("table Name");
		} catch (RuntimeException e) {
			System.out.println("操作回滚了");
		}
	}

	/**
	 * 测试h2数据库
	 */
	@Test
	public void testH2() {
		List<UserVo> userList = new LinkedList<>();
		for(int i=0; i<10000; i++){
			userList.add(new UserVo("name"+i,i));
		}
		int age = us.testH2("user", userList);
		System.out.println(age);
	}

	/**
	 * 多数据源测试
	 */
	@Test
	public void testDataSource() {
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
	public void testMybatisCache() {
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
	public void testTransactional() {
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
	public void testPage() {
		List<UserVo> allUserPage = us.getAllUserPage(-1);
		
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
	@SuppressWarnings("all")
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
	public void testLanguage() {
		System.out.println(messageSource.getMessage("welcome", null, Locale.CHINA));
		System.out.println(messageSource.getMessage("welcome", null, Locale.ENGLISH));
	}

	/**
	 * JSON 与 Object 互转
	 * 
	 * @throws IOException
	 */
	@Test
	public void testJson(){

        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(new User("张三" + i));
        }

        // toJSON
        String jsonObject = CommonUtil.toJSON(userList.get(0));
        System.err.println(jsonObject);

        // toJSON
        String jsonObjectList = CommonUtil.toJSON(userList);
        System.err.println(jsonObjectList);

        // toObject
        System.err.println(CommonUtil.getObject(jsonObject, new TypeReference<User>() {
        }));

        // toObjectList 方法
        List<User> value = CommonUtil.getObject(jsonObjectList, new TypeReference<List<User>>() {
        });
        System.err.println(value);
    }
	
	@Test
	@SuppressWarnings("unused")
	public void testSpringUtils() throws Exception {
		
		// Bean复制
		{
			User user = new User();
			BeanUtils.copyProperties(new User("张三"), user);
			System.out.println();
		}
		
		// 反射工具
		{
			Field findField = ReflectionUtils.findField(User.class, "name");
			System.out.println();
		}
		
		// ant匹配工具
		{
			boolean match = new AntPathMatcher().match("/g/**/a", "/g/x/a");
			System.out.println();
		}
		
		// 断言
		{
			Assert.isNull(null,"啦啦啦");
			System.out.println();
		}
		
		// http请求资源类型
		{
			String value = MimeTypeUtils.APPLICATION_JSON_VALUE;
			System.out.println();
		}
		
		// 占位符替换
		{
			PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("#{", "}") ;
			
			Properties properties = new Properties();
			properties.put("accountNo", "1008");
			properties.put("money", "300");
			String placeholders = helper.replacePlaceholders("【蚂蚁借呗】你的支付宝#{accountNo}将从余额、储蓄卡或余额宝自动还款#{money}元", properties );
			System.out.println();
		}
		
		// 资源工具
		{
			File file = ResourceUtils.getFile("classpath:application.yml");
			byte[] byteArray = FileCopyUtils.copyToByteArray(file);
			String text = new String(byteArray);
			System.out.println();
		}
		
		// ID生成器
		{
			SimpleIdGenerator simpleIdGenerator = new SimpleIdGenerator();
			Long id = simpleIdGenerator.generateId().getLeastSignificantBits();
			id = simpleIdGenerator.generateId().getLeastSignificantBits();
			id = simpleIdGenerator.generateId().getLeastSignificantBits();
			id = simpleIdGenerator.generateId().getLeastSignificantBits();
			id = simpleIdGenerator.generateId().getLeastSignificantBits();
			id = simpleIdGenerator.generateId().getLeastSignificantBits();
			System.out.println();
		}
		
		// 找到可用端口
		{
			SortedSet<Integer> tcpPorts = SocketUtils.findAvailableTcpPorts(3);
			System.out.println();
		}
		
		// 字符串工具
		{
			Set<String> set = StringUtils.commaDelimitedListToSet("a,b,c,d,e,f,g");
			String delimitedString = StringUtils.arrayToCommaDelimitedString(set.toArray());
			System.out.println();
		}
		
		// 文件复制工具
		{
			File file = ResourceUtils.getFile("classpath:application.yml");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(FileCopyUtils.copyToByteArray(file));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			FileCopyUtils.copy(inputStream, outputStream);
		}
		
		// 输入输出流工具
		{
			File file = ResourceUtils.getFile("classpath:application.yml");
			ByteArrayInputStream inputStream = new ByteArrayInputStream(FileCopyUtils.copyToByteArray(file));
			String string = StreamUtils.copyToString(inputStream,StandardCharsets.UTF_8);
			System.out.println();
		}
		
		// 集合工具
		{
			boolean empty = CollectionUtils.isEmpty(new ArrayList<>());
			System.out.println();
		}
	}

}
