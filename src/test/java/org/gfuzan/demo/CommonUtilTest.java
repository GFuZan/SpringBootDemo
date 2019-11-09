package org.gfuzan.demo;

import org.gfuzan.RunApplication;
import org.gfuzan.common.utils.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RunApplication.class },webEnvironment=WebEnvironment.RANDOM_PORT)
public class CommonUtilTest {

	ObjectMapper om = new ObjectMapper();

	/**
	 * 执行表达式-对象测试
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
}
