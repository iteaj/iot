package com.iteaj.iot.redis.consumer;

public interface BlockConsumer {

    /**
     * 阻塞线程消费
     * @return true 将会开启一个线程阻塞等待处理, 尽量少使用阻塞操作
     */
    default boolean block() {
        return false;
    }
}
