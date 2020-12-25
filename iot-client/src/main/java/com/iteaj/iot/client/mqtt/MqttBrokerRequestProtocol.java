package com.iteaj.iot.client.mqtt;

import com.iteaj.iot.client.IotClientBootstrap;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.ServerRequestClientProtocol;
import com.iteaj.iot.client.mqtt.common.MessageData;
import com.iteaj.iot.client.mqtt.protocol.ClientProtocolUtil;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.message.MqttClientMessage;

import java.util.concurrent.Executor;

/**
 * 设备主动发布的mqtt的协议, 由mqtt broker推送给客户端的协议
 */
public abstract class MqttBrokerRequestProtocol extends ServerRequestClientProtocol {

    public MqttBrokerRequestProtocol(MqttClientMessage requestMessage) {
        super(requestMessage, null);
    }

    protected Executor getExecutor() {
        return IotClientBootstrap.taskExecutor;
    }

    /**
     * 此方法将响应对应的报文给设备
     */
    public void publish() {
        buildResponseMessage();
    }

    @Override
    public MqttBrokerRequestProtocol buildRequestMessage() {
        if(requestMessage() == null) throw new ProtocolException("无请求报文");
        if(logger.isDebugEnabled()) {
            logger.debug("设备请求客户端(MQTT) - 请求报文: {}", requestMessage);
        }

        // 构建完请求报文之后
        doBuildRequestMessage((MqttClientMessage) requestMessage);

        if(isAsyncExec()) { // 异步执行业务
            getExecutor().execute(() -> {
                try {
                    /**
                     * @see #publish() 如果需要响应报文, 则需要在业务方法里面手动调用次方法发布此次请求的响应
                     * 调用协议对应的业务
                     */
                    this.exec(IotClientBootstrap.businessFactory);
                } catch (Exception e) {
                    logger.error("业务执行异常: {}", e.getMessage(), e);
                }
            });
        } else {
            this.exec(IotClientBootstrap.businessFactory); // 同步执行业务

            this.publish(); // 处理完业务发布响应报文
        }

        return this;
    }

    protected abstract void doBuildRequestMessage(MqttClientMessage requestMessage);

    @Override
    public MqttBrokerRequestProtocol buildResponseMessage() {
        // 构建要发布的报文信息
        MessageData messageData = doBuildResponseMessage();

        // 发布报文
        IotNettyClient client = this.getNettyClient();
        client.getChannel().writeAndFlush(ClientProtocolUtil.publishMessage(messageData));

        return this;
    }

    protected abstract IotNettyClient getNettyClient();

    protected abstract MessageData doBuildResponseMessage();

    @Override
    public boolean isRelation() {
        return false;
    }

    @Override
    public Object relationKey() {
        return null;
    }

    @Override
    public String desc() {
        return "Mqtt Broker发布给客户端协议";
    }
}
