package com.iteaj.iot.client.mqtt.common;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class SubscribeMessage {

    private int qos;
    private String topic;

    public static SubscribeMessage builder() {
        return new SubscribeMessage();
    }

    public String getTopic() {
        return topic;
    }

    public SubscribeMessage topic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getQos() {
        return qos;
    }

    public SubscribeMessage qos(int qos) {
        this.qos = qos;
        return this;
    }
}