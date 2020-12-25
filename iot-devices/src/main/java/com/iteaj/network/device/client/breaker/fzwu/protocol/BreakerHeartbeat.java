package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;

public class BreakerHeartbeat extends BreakerDeviceRequestProtocol {
    public BreakerHeartbeat(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_20;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {

    }
}
