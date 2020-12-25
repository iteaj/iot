package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;

/**
 * 断路器上报电量数据
 * @see BreakerType#BH_35
 */
public class BreakerPowerData extends BreakerDeviceRequestProtocol {

    public BreakerPowerData(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_35;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {

    }
}
