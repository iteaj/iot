package com.iteaj.network.device.client.breaker.fzwu;

import cn.hutool.core.util.RandomUtil;
import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.mqtt.IotMqttClientAbstract;
import com.iteaj.iot.client.mqtt.api.MqttConsumerListener;
import com.iteaj.iot.client.mqtt.api.MqttConsumerProcess;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.List;

public class BreakerMqttClient extends IotMqttClientAbstract {

    public BreakerMqttClient(int port, String host, List<MqttConsumerListener> listenerList, ClientComponent clientComponent) {
        super(port, host, listenerList, clientComponent);
    }

    @Override
    protected String getClientId() {
        return "Breaker_Mqtt_" + RandomUtil.randomUUID();
    }

    @Override
    protected void subscribeTopic(MqttConsumerProcess process) {
        consumerProcess.subscribe("YINDE/LINKCLOSE/#", MqttQoS.EXACTLY_ONCE.value());
        consumerProcess.subscribe("YINDE/LINKtoSERVER/#", MqttQoS.EXACTLY_ONCE.value());
    }
}
