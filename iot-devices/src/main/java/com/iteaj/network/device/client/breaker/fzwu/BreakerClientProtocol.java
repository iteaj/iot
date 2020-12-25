package com.iteaj.network.device.client.breaker.fzwu;

import cn.hutool.core.util.RandomUtil;
import com.iteaj.iot.client.IotClientBootstrap;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.mqtt.common.MessageData;
import com.iteaj.iot.client.mqtt.protocol.MqttClientProtocol;
import io.netty.handler.codec.mqtt.MqttQoS;

public abstract class BreakerClientProtocol extends MqttClientProtocol {

    private String gateway; // 网关编号
    /**
     *
     * @param sn 断路器编号
     * @param gateway 网关编号
     */
    public BreakerClientProtocol(String sn, String gateway) {
        super(sn, "YINDE/SERVERtoLINK/"+gateway);
        this.gateway = gateway;
    }

    @Override
    public MessageData doBuildRequestMessage() {
        byte[] breakerData = this.getBreakerData();
        this.requestMessage = BreakerUtils.buildBreakerRequestMessage(protocolType().value, getEquipCode(), breakerData);

        // 构建断路器报文
        byte[] payload = BreakerUtils.doBuildPayload((BreakerMessage) this.requestMessage);
        if(logger.isDebugEnabled()) {
            logger.debug("客户端请求设备(MQTT) - 请求报文： {}", this.requestMessage);
        }

        MessageData messageData = MessageData.builder()
                .messageId(RandomUtil.randomInt()).qos(MqttQoS.AT_LEAST_ONCE.value())
                .retained(false).dup(false).topic(getTopicName())
                .timestamp(System.currentTimeMillis()).payload(payload);

        return messageData;
    }

    /**
     * 断路器的messageId由网关和断路器编号以及请求的协议来确定唯一
     * messageId转成大写
     * @return
     */
    @Override
    protected String getMessageId() {
        return (gateway+getEquipCode()+protocolType().value).toUpperCase();
    }


    @Override
    protected IotNettyClient getIotNettyClient() {
        return IotClientBootstrap.getClient(BreakerMessage.class);
    }

    /**
     * 返回断路器报文里面的数据域
     * @return
     */
    protected abstract byte[] getBreakerData();

    @Override
    public abstract BreakerType protocolType();

    @Override
    public String desc() {
        return "断路器协议 - " + protocolType();
    }

    public String getGateway() {
        return gateway;
    }
}
