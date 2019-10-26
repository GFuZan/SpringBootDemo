package org.gfuzan.modules.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.gfuzan.modules.entity.User;
import org.gfuzan.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("testController")
public class TestController {
	@Autowired
	private UserService us;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 多语言测试1
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(path = { "getPage" }, method = RequestMethod.GET)
	public String getPage(Model model) {

		String welcome = messageSource.getMessage("welcome", null, Locale.ENGLISH);
		model.addAttribute("welcome", welcome);
		model.addAttribute("userList", us.getAllUser());
		return "test";
	}

	/**
	 * 多语言测试2
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(path = { "getPage2" }, method = RequestMethod.GET)
	public String getPage2(Model model) {
		String welcome = messageSource.getMessage("welcome", null, Locale.CHINA);
		model.addAttribute("welcome", welcome);
		model.addAttribute("userList", us.getAllUser2());
		return "test";
	}

	/**
	 * 缓存测试
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(path = { "testCache" }, method = RequestMethod.GET)
	@ResponseBody
	public Object testCache(Boolean update) {
		if (update != null && update == true) {
			return us.updateUser();
		} else {
			return us.getAllUser();
		}
	}

	/**
	 * 参数验证
	 * 
	 * @param user
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(path = { "testValid" }, method = RequestMethod.POST)
	@ResponseBody
	public String testValid(@RequestBody @Valid User user, BindingResult bindingResult) {

		StringBuilder sb = new StringBuilder();

		if (bindingResult.hasErrors()) {
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			for (ObjectError oe : allErrors) {
				sb.append(oe.getDefaultMessage()).append('\n');
			}
		}

		return sb.toString();
	}
}
