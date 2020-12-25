package com.iteaj.network.device.client.breaker.fzwu;

import com.iteaj.iot.client.mqtt.api.MqttConsumerListener;
import com.iteaj.iot.client.mqtt.common.MessageData;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.IotServeBootstrap;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.ProtocolTimeoutStorage;
import com.iteaj.network.device.client.breaker.fzwu.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BreakerConsumerListener extends ProtocolFactory<BreakerMessage> implements MqttConsumerListener {

    private Logger logger = LoggerFactory.getLogger(BreakerConsumerListener.class);

    public BreakerConsumerListener(ProtocolTimeoutStorage delegation) {
        super(delegation);
    }

    @Override
    public boolean matcher(String topic) {
        return topic.startsWith("YINDE/LINK");
    }

    @Override
    public void receiveMessage(int msgId, String topic, MessageData msg) {

    }

    @Override
    public void receiveMessageByAny(int msgId, String topic, MessageData msg) {
        byte[] payload = msg.getPayload();
        // 设备下线处理
        if(topic.startsWith("YINDE/LINKCLOSE")) {
            deviceOfflineHandle(topic);
            return;
        }
        BreakerMessage breakerMessage = BreakerUtils.buildBreakerRequestMessage(payload, topic);
        if(!"16".equals(breakerMessage.getOver())) { // 报文的终止符必须为16, 否则说明报文错误
            logger.error("错误的断路器终止符: {}, 报文: ", breakerMessage.getOver(), breakerMessage);
            return;
        }

        this.getProtocol(breakerMessage);
    }

    private void deviceOfflineHandle(String topic) {
        String gatewayOfTopic = BreakerUtils.getGatewayOfTopic(topic);
        BreakerMessage breakerMessage = new BreakerMessage();
        breakerMessage.setGateway(gatewayOfTopic);
        new BreakerOfflineHandle(breakerMessage).buildRequestMessage();
    }

    private void clientRequestProtocolHandle(BreakerMessage breakerMessage) {
        String messageId = breakerMessage.getGateway() + breakerMessage.getAddr() + breakerMessage.getType();

        try {
            /**
             * @see BreakerClientProtocol#getMessageId()
             */
            BreakerClientProtocol protocol = (BreakerClientProtocol)remove(messageId.toUpperCase());
            if(protocol != null) {
                protocol.setResponseMessage(breakerMessage).buildResponseMessage();
            } else {
                if(BreakerType.BH_37.value.equalsIgnoreCase(breakerMessage.getType())) {
                    new BreakerStatusProtocol(breakerMessage.getDeviceSn(), breakerMessage.getGateway()
                            , null).setResponseMessage(breakerMessage).buildResponseMessage();
                    return;
                } else if(BreakerType.BH_4E.value.equalsIgnoreCase(breakerMessage.getType())) {
                    new Breaker4PStatusProtocol(breakerMessage.getDeviceSn(), breakerMessage.getGateway()
                            , null).setResponseMessage(breakerMessage).buildResponseMessage();
                    return;
                }

                logger.warn("获取客户端请求协议失败, 协议不存在可能已经超时或者已移除, 报文: {}", breakerMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractProtocol getProtocol(BreakerMessage breakerMessage) {
        switch (breakerMessage.getType()) {
            case "20":
                new BreakerHeartbeat(breakerMessage).buildRequestMessage();
                break;
            case "31":
                new DateProtocol(breakerMessage).buildRequestMessage();
                break;
            case "32":
                new BreakerGatewayInfo(breakerMessage).buildRequestMessage();
                break;
            case "33":
                new BreakerInfo(breakerMessage).buildRequestMessage();
                break;
            case "35": // 上报断路器数据--35H
                new BreakerRealData(breakerMessage).buildRequestMessage();
                break;
            case "36":
                new BreakerErrorWarning(breakerMessage).buildRequestMessage();
                break;
            case "37":
            case "41":
            case "4a":
            case "4e":
            case "40":
                this.clientRequestProtocolHandle(breakerMessage);
                break;
            case "48":
                new BreakerThreeRealData(breakerMessage).buildRequestMessage();
                break;
            default:
                System.out.println(breakerMessage.getType());
        }

        return null;
    }
}
