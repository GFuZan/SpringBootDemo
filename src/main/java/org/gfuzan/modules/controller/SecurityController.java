package org.gfuzan.modules.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecurityController {

	@RequestMapping("login")
	public String login(Model model) {
		return "login";
	}

	/**
	 * 管理员页面
	 * 
	 * @return
	 */
	@RequestMapping("admin")
	@ResponseBody
	public String admin() {
		return "admin";
	}

	/**
	 * 用户页面
	 * 
	 * @return
	 */
	@RequestMapping("user")
	@ResponseBody
	public String user() {
		return "user";
	}

	/**
	 * 运维人员页面
	 * 
	 * @return
	 */
	@RequestMapping("test")
	@ResponseBody
	public String test() {
		return "test";
	}
}
