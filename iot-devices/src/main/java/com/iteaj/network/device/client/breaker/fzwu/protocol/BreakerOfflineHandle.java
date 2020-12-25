package com.iteaj.network.device.client.breaker.fzwu.protocol;


import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;
import com.iteaj.network.message.MqttClientMessage;

/**
 * 断路器下线处理
 */
public class BreakerOfflineHandle extends BreakerDeviceRequestProtocol {

    public BreakerOfflineHandle(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.Offline;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {

    }
}
