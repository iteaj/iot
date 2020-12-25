package com.iteaj.network.device.client.breaker.fzwu;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.ClientProperties;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.mqtt.api.MqttConsumerListener;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.client.ClientMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BreakerClientComponent extends ClientComponent {

    @Autowired
    private ClientProperties properties;

    @Override
    protected IotNettyClient createNettyClient() {
        ClientProperties.MqttClientConfig mqtt = properties.getMqtt();
        if(null == mqtt) {
            throw new IllegalArgumentException("未指定Mqtt Broker主机和端口");
        }
        List<MqttConsumerListener> listeners = new ArrayList<>();
        listeners.add((MqttConsumerListener) protocolFactory());
        return new BreakerMqttClient(mqtt.getPort(), mqtt.getHost(), listeners, this);
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new BreakerConsumerListener(protocolTimeoutStorage());
    }

    @Override
    public String name() {
        return "断路器Mqtt客户端";
    }

    @Override
    public Class<? extends ClientMessage> messageClass() {
        return BreakerMessage.class;
    }

}
