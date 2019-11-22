package org.gfuzan.demo;

import java.util.ArrayList;
import java.util.List;

import org.gfuzan.RunApplication;
import org.gfuzan.common.utils.CommonUtil;
import org.gfuzan.common.utils.CommonUtil.ObjectWrapper;
import org.gfuzan.modules.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author GFuZan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RunApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommonUtilTest {

	ObjectMapper om = new ObjectMapper();

	/**
	 * 执行表达式-对象测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExpression1() throws Exception {
		Object res = null;
		res = CommonUtil.getExpressionResult("1/2+2*(100+3)");

		System.out.println(res);
	}

	/**
	 * 执行表达式-对象测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExpression2() throws Exception {
		Object res = null;
		res = CommonUtil.getExpressionResult("var a = {A:1,B:2}; a");

		if (res.getClass().getSimpleName().equals("ScriptObjectMirror")) {
			res = om.readValue(om.writeValueAsString(res), Object.class);
		}

		System.out.println(res);
	}

	/**
	 * 执行表达式-执行Java对象
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExpression3() throws Exception {
		Object res = null;

		String js = "" 
				+ "var CommonUtil = Java.type(\"org.gfuzan.common.utils.CommonUtil\");"
				+ "var om = CommonUtil.getObjectMapper();" 
				+ "om.writeValueAsString({a:1,B:2});";

		res = CommonUtil.getExpressionResult(js);

		if (res.getClass().getSimpleName().equals("ScriptObjectMirror")) {
			res = om.readValue(om.writeValueAsString(res), Object.class);
		}

		System.out.println(res);
	}

	/**
	 * 执行表达式-执行Spring bean
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExpression4() throws Exception {
		Object res = null;

		String js = "" 
				+ "var CommonUtil = Java.type(\"org.gfuzan.common.utils.CommonUtil\");"
				+ "var springContext = CommonUtil.getSpringContext();"
				+ "var userService = springContext.getBean(Java.type(\"org.gfuzan.modules.service.UserService\").class);"
				+ "userService.getAllUser();";

		res = CommonUtil.getExpressionResult(js);

		if (res.getClass().getSimpleName().equals("ScriptObjectMirror")) {
			res = om.readValue(om.writeValueAsString(res), Object.class);
		}

		System.out.println(res);
	}

	/**
	 * 线程安全性测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExpression7() throws Exception {

		String js = "var a = a ? a : 1 ; a++;";

		{
			ObjectWrapper<Integer> ow = new ObjectWrapper<>();
			ow.setValue(0);
			new Thread(() -> {
				while (true) {
					ow.setValue(ow.getValue() + 1);
					Object result = CommonUtil.getExpressionResult(js);
					System.out.println("执行次数: " + ow.getValue() + " [a = " + result + "]");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		{
			ObjectWrapper<Integer> ow = new ObjectWrapper<>();
			ow.setValue(0);
			new Thread(() -> {
				while (true) {
					ow.setValue(ow.getValue() + 1);
					Object result = CommonUtil.getExpressionResult(js);
					System.out.println("执行次数: " + ow.getValue() + " [a = " + result + "]");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		
		while (true) {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	   /**
     * 线程安全性测试
     * 
     * @throws Exception
     */
    @Test
    public void testToJSONGetObject() {

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
}
