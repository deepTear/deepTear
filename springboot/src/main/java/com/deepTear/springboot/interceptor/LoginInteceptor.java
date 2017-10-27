package com.deepTear.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.deepTear.springboot.entity.User;
import com.deepTear.springboot.properties.SysDataPropertiesUtils;

public class LoginInteceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String url = request.getRequestURI();
		String exclude = SysDataPropertiesUtils.getLogin_exclude();
		if(exclude != null){
			String[] ex_urls = exclude.split(",");
			for(String ex : ex_urls){
				if(url.contains(ex)){
					return true;
				}
			}
		}

		User loginUser = (User)request.getSession().getAttribute("LOGIN_USER");
		if(loginUser == null){
			String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
			String type = request.getHeader("X-Requested-With") == null ? "" : request.getHeader("X-Requested-With");// XMLHttpRequest
			if ("XMLHttpRequest".equals(type)) {
                // 处理ajax请求
				response.setHeader("SESSIONSTATUS", "TIMEOUT");
				response.setHeader("CONTEXTPATH", basePath + "public/login.html");
				//hres.setStatus(HttpServletResponse.SC_FORBIDDEN);
				//request.getWriter().write("");
				return false;
            }else{
            	response.sendRedirect(basePath + "public/login.html");
            	return false;
            }
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
