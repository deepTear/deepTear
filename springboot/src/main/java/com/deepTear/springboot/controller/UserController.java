package com.deepTear.springboot.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.deepTear.springboot.entity.User;
import com.deepTear.springboot.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {

	private static Log log = LogFactory.getLog(User.class.getName());

	@Autowired
	private UserService userService;

	@RequestMapping(value="/pageList")
	public ModelAndView pageList(){
		ModelAndView mav = new ModelAndView("user/userList");
		return mav;
	}

	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String addPage(){
		return "user/userAdd";
	}

	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(User user){
		userService.addUser(user);
		log.info("添加新用户");
		return "redirect:/user/add";
	}
}
