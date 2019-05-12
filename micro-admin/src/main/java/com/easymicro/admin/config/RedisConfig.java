package com.easymicro.admin.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * ehcache配置
 *
 * @author fengshuonan
 * @date 2017-05-20 23:11
 */
@Configuration
@EnableCaching
public class RedisConfig {

}
