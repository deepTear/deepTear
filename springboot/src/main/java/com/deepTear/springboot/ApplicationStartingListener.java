package com.deepTear.springboot;

import javax.servlet.ServletContextEvent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot启动开始时执行的事件
 * @author Administrator
 *
 */
public class ApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent>{

	public static final String log4jdirkey = "log4jdir";

	@Override
	public void onApplicationEvent(ApplicationStartingEvent event) {
		SpringApplication app = event.getSpringApplication();
        System.out.println("服务器启动中");
        //event.getSpringApplication().
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
        System.getProperties().remove(log4jdirkey);
    }

    public void contextInitialized(ServletContextEvent servletcontextevent) {
    	String log4jdir = servletcontextevent.getServletContext().getRealPath("/");
    	//System.out.println("log4jdir:"+log4jdir);
    	System.setProperty(log4jdirkey, log4jdir);
    }

}
