package com.iteaj.network.message;

import com.iteaj.network.client.ClientMessage;

public class MqttClientMessage extends ClientMessage {

    private String topic;

    public MqttClientMessage(byte[] message) {
        super(null, message);
    }

    public MqttClientMessage(byte[] message, String topic) {
        super(null, message);
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public MessageHead getHead() {
        return null;
    }

    @Override
    public MessageBody getBody() {
        return null;
    }
}
