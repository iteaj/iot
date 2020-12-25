package com.iteaj.iot.redis;

import com.iteaj.iot.redis.producer.RedisProducer;
import com.iteaj.network.AbstractProtocol;
import org.springframework.data.redis.core.RedisTemplate;

public interface ComplexRedisProducer<T extends AbstractProtocol> extends RedisProducer<T> {

    /**
     * 持久化到redis
     * @param protocol
     */
    void producer(T protocol, RedisTemplate template);
}
