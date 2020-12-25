package com.iteaj.iot.redis.producer;

import com.iteaj.iot.redis.IotRedis;
import com.iteaj.network.Protocol;

import java.util.List;

/**
 * 数据生产者
 * @param <T>
 */
public interface RedisProducer<T extends Protocol> extends IotRedis {

    /**
     * redis 数据生产者
     * @see com.iteaj.iot.redis.consumer.RedisConsumer#consumer(List) 将由此方法消费
     * @param protocol
     * @return 将直接序列化到redis
     */
    Object business(T protocol);

    /**
     * 持久化到redis
     * @param value
     */
    void persistence(Object value);
}
