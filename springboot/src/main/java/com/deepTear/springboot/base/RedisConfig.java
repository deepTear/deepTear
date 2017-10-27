package com.deepTear.springboot.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
@PropertySource(value="classpath:/config/redis.properties")
public class RedisConfig {

	@Bean(name= "jedis.pool")
    @Autowired
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,
                @Value("${jedis.pool.host}")String host,
                @Value("${jedis.pool.port}")int port,
                @Value("${connectionTimeout}")int timeout,
                @Value("${jedis.auth.pass}")String auth) {
        return new JedisPool(config,host,port,timeout,auth);
    }

    @Bean(name= "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig (@Value("${jedis.pool.config.maxTotal}")int maxTotal,
                                @Value("${jedis.pool.config.maxIdle}")int maxIdle,
                                @Value("${jedis.pool.config.maxWaitMillis}")int maxWaitMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        return config;
    }
}
