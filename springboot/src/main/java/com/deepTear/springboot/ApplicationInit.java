package com.deepTear.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource(value = "classpath:/config/*.properties", ignoreResourceNotFound = true)
public class ApplicationInit {

    public static void main(String[] args) {
        //SpringApplication.run(ApplicationInit.class, args);
    	SpringApplication app = new SpringApplication(ApplicationInit.class);
        app.addListeners(new ApplicationStartingListener());
        app.addListeners(new ApplicationEnvironmentPreparedListener());
        app.run(args);
    }
}

