package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.client.ClientMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerClientProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;

/**
 * 下发漏电自检
 */
public class BreakerSelfChecking extends BreakerClientProtocol {

    public BreakerSelfChecking(String sn, String gateway) {
        super(sn, gateway);
    }

    @Override
    protected byte[] getBreakerData() {
        return new byte[0];
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_40;
    }

    @Override
    public void doBuildResponseMessage(ClientMessage message) {

    }
}
