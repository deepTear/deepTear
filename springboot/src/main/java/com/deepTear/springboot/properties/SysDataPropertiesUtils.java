package com.deepTear.springboot.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource(value = "classpath:/config/sys-data.properties", ignoreResourceNotFound = true,encoding="utf-8")
public class SysDataPropertiesUtils {

    //private static Environment env;

	private static String login_exclude;

	/*public static Environment getEnv() {
		return env;
	}*/

	/*@Autowired
	public void setEnv(Environment env) {
		SysDataPropertiesUtils.env = env;
	}*/



	public static String getLogin_exclude() {
		return login_exclude;
	}

	@Value("${login.exclude}")
	public void setLogin_exclude(String login_exclude) {
		SysDataPropertiesUtils.login_exclude = login_exclude;
	}

}
