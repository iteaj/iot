package com.iteaj.network.device.client.breaker.fzwu;

import com.iteaj.iot.client.ClientProtocolService;
import com.iteaj.network.consts.ExecStatus;

public abstract class BreakerClientService<T extends BreakerClientProtocol> extends ClientProtocolService<T> {

    @Override
    public Object business(T protocol) {
        return super.business(protocol);
    }

    @Override
    protected Object doFail(T protocol, ExecStatus status) {
        return null;
    }

    @Override
    public abstract BreakerType protocolType();
}
