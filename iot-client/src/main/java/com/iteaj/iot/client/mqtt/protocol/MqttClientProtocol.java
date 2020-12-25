package com.iteaj.iot.client.mqtt.protocol;

import com.iteaj.iot.client.ClientRequestProtocol;
import com.iteaj.iot.client.IotClientBootstrap;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.mqtt.common.MessageData;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.client.ClientMessage;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

public abstract class MqttClientProtocol extends ClientRequestProtocol {

    private String topicName;
    private MessageData messageData;

    public MqttClientProtocol(String topicName) {
        this.topicName = topicName;
    }

    public MqttClientProtocol(String equipCode, String topicName) {
        super(equipCode);
        this.topicName = topicName;
    }

    @Override
    public MqttClientProtocol request() throws ProtocolException {
        AbstractProtocol abstractProtocol = buildRequestMessage();
        if(abstractProtocol == null) throw new ProtocolException("构建协议失败");

        IotNettyClient client = getIotNettyClient();
        MqttPublishMessage mqttPublishMessage = ClientProtocolUtil.publishMessage(this.messageData);
        ChannelFuture channelFuture = client.getChannel().writeAndFlush(mqttPublishMessage);

        if(logger.isDebugEnabled()) {
            logger.debug("发送Mqtt报文({}) 协议类型: {} - 报文: {}", client.getName(), protocolType(), this.messageData);
        }

        if(channelFuture != null && isRelation()) {
            IotClientBootstrap.getTimeoutStorage((Class<? extends ClientMessage>) requestMessage.getClass())
                    .add(relationKey(), this, getTimeout());
        }

        return this;
    }

    @Override
    public MqttClientProtocol buildRequestMessage() {
        this.messageData = this.doBuildRequestMessage();
        return this;
    }

    public abstract MessageData doBuildRequestMessage();

    @Override
    protected abstract String getMessageId();

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
