package com.deepTear.springboot;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot 对应Enviroment已经准备完毕，但此时上下文context还没有创建。
 * @author Administrator
 *
 */
public class ApplicationEnvironmentPreparedListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>{

	@Override
	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		System.out.println("应用环境启动完成");
	}

}
