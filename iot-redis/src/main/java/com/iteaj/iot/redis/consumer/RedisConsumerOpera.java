package com.iteaj.iot.redis.consumer;

import com.alibaba.fastjson.JSONObject;
import com.iteaj.iot.redis.IotRedis;

import java.util.List;

public interface RedisConsumerOpera extends IotRedis {

    /**
     * 获取值
     * @return
     */
    List invoker(String key, int size);

    /**
     * 反序列化
     * @param value
     * @param clazz
     * @return
     */
    List deserialize(List<?> value, Class clazz);

    /**
     * 移除已经消费的列表元素
     * @param key
     * @param size
     */
    void remove(String key, int size);

    /**
     * 普通的消费列表
     * @return
     */
    List<RedisConsumer> consumers();
}
