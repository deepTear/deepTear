package com.deepTear.springboot;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot启动异常时执行事件
 * @author Administrator
 *
 */
public class ApplicationFailedListener implements ApplicationListener<ApplicationFailedEvent>{

	@Override
	public void onApplicationEvent(ApplicationFailedEvent event) {

	}

}
