package com.iteaj.network.device.client.breaker.fzwu;

import com.iteaj.iot.client.IotClientBootstrap;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.mqtt.MqttBrokerRequestProtocol;
import com.iteaj.iot.client.mqtt.common.MessageData;
import io.netty.handler.codec.mqtt.MqttQoS;

public abstract class BreakerDeviceRequestProtocol extends MqttBrokerRequestProtocol {

    public BreakerDeviceRequestProtocol(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected MessageData doBuildResponseMessage() {
        BreakerMessage message = (BreakerMessage)requestMessage();
        String topic = message.getTopic();
        String gatewayId = topic.split("/")[2];

        this.responseMessage = BreakerUtils.buildBreakerMessageFormRequestMessage((BreakerMessage) requestMessage);
        this.setResponseData((BreakerMessage) this.responseMessage);

        byte[] payload = BreakerUtils.doBuildPayload((BreakerMessage) this.responseMessage);

        MessageData messageData = MessageData.builder()
                .messageId(1).qos(MqttQoS.AT_LEAST_ONCE.value())
                .retained(false).dup(false).topic("YINDE/SERVERtoLINK/"+gatewayId)
                .timestamp(System.currentTimeMillis()).payload(payload);

        return messageData;
    }

    @Override
    protected IotNettyClient getNettyClient() {
        return IotClientBootstrap.getClient(BreakerMessage.class);
    }

    public String getSn() {
        BreakerMessage breakerMessage = (BreakerMessage) requestMessage();
        if(breakerMessage == null) return null;

        return breakerMessage.getDeviceSn();
    }

    @Override
    public String getEquipCode() {
        return this.getSn();
    }

    protected void setResponseData(BreakerMessage responseMessage) {

    }

    @Override
    public abstract BreakerType protocolType();
}
