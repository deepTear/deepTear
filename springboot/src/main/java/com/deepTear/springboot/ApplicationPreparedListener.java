package com.deepTear.springboot;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * spring boot上下文context创建完成，但此时spring中的bean是没有完全加载完成的。
 * @author Administrator
 *
 */
public class ApplicationPreparedListener implements ApplicationListener<ApplicationPreparedEvent>{

	@Override
	public void onApplicationEvent(ApplicationPreparedEvent event) {
		// TODO Auto-generated method stub

	}

}
