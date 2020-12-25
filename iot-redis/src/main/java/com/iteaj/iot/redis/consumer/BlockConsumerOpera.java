package com.iteaj.iot.redis.consumer;

import java.util.List;

public interface BlockConsumerOpera extends RedisConsumerOpera{


    /**
     * 阻塞的消费列表
     * @return
     */
    List<RedisConsumer> blocks();

    /**
     * 阻塞获取值
     * @param timeout
     * @return
     */
    List invoker(String key, long timeout);
}
