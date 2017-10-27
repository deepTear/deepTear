package com.deepTear.springboot.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deepTear.springboot.entity.User;
import com.deepTear.springboot.service.UserService;
import com.deepTear.springboot.utils.ValidateCode;

@Controller
@RequestMapping(value="/")
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping(value="login",method = {RequestMethod.POST})
	public String login(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String validateCode = request.getParameter("code");
		String username = request.getParameter("username");
		String userpass = request.getParameter("userpass");

		HttpSession session = request.getSession();
		String code = (String)session.getAttribute("LOGIN_CODE");
		Date code_create_date = (Date)session.getAttribute("LOGIN_CODE_CREATE_DATE");
		Date current_date = new Date();

		if(code == null || code_create_date == null){
			return "redirect:/public/login.html";
		}

		if(current_date.getTime() - code_create_date.getTime() > 120 * 1000){
			request.setAttribute("ERROR",1);
			return "redirect:/public/login.html";
		}

		if(!code.equals(validateCode)){
			request.setAttribute("ERROR",2);
			return "redirect:/public/login.html";
		}

		List<User> us = userService.findByUserAccount(username);
		if(us != null && us.size() > 0){
			User login = us.get(0);
			if(login.getPassword().equals(userpass)){
				session.setAttribute("LOGIN_USER",login);
				return "redirect:/";
			}else{
				request.setAttribute("ERROR",3);
			}
		}else{
			request.setAttribute("ERROR",4);
		}

		return "redirect:/public/login.html";
	}


	@RequestMapping(value="validate_code")
	@ResponseBody
	public void createValidateCode(HttpServletRequest request,HttpServletResponse response){
		try {
			HttpSession session = request.getSession();
			ValidateCode validate_code = new ValidateCode();
			Map<String,Object> map = validate_code.createValidateCodeImage();
			String code = (String)map.get("code");
			BufferedImage image = (BufferedImage)map.get("image");
			ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
			JPEGImageWriteParam params = new JPEGImageWriteParam(null);
			ImageOutputStream ios = ImageIO.createImageOutputStream(response.getOutputStream());
			writer.setOutput(ios);
			writer.write(null, new IIOImage(image, null, null),params);
			writer.dispose();
			ios.close();
			session.setAttribute("LOGIN_CODE",code);
			session.setAttribute("LOGIN_CODE_CREATE_DATE",new Date());
			response.setContentType("image/jpeg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value="logout")
	public String logout(HttpServletRequest request){
		request.getSession().removeAttribute("LOGIN_USER");
		return "redirect:/public/login.html";
	}

}
