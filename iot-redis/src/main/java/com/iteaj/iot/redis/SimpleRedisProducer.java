package com.iteaj.iot.redis;

import com.iteaj.iot.redis.producer.RedisProducer;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.Protocol;
import org.springframework.data.redis.core.BoundKeyOperations;

/**
 * @see AbstractProtocol
 * 一种协议只支持单个Key
 * @param <T>
 */
public interface SimpleRedisProducer<T extends Protocol, O> extends RedisProducer<T> {

    /**
     * 返回 redis key
     * @return
     */
    String getKey();

    O operation();
}
