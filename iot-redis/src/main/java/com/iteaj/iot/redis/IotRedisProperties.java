package com.iteaj.iot.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iot.redis")
public class IotRedisProperties {

    /**
     * 是否是消费者
     */
    private boolean consumer = false;

    /**
     * 是否是生产者
     */
    private boolean producer = false;

    /**
     * 消费者线程数量
     */
    private short consumerThread = 5;

    public boolean isConsumer() {
        return consumer;
    }

    public void setConsumer(boolean consumer) {
        this.consumer = consumer;
    }

    public boolean isProducer() {
        return producer;
    }

    public void setProducer(boolean producer) {
        this.producer = producer;
    }
}
