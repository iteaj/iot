package com.iteaj.iot.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public interface IotRedis {

    RedisTemplateWrapper wrapper = new RedisTemplateWrapper();

    default RedisTemplate template() {
        return wrapper.template;
    }

    /**
     * @see IotRedisConfiguration#jsonRedisTemplate(RedisConnectionFactory) 初始化
     */
    class RedisTemplateWrapper {
        public static RedisTemplate template;
    }
}
