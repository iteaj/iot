package com.iteaj.iot.client;

import com.iteaj.network.client.NettyClientHandle;
import com.iteaj.network.consts.ExecStatus;

public abstract class ClientProtocolService<T extends ClientRequestProtocol> implements NettyClientHandle<T> {

    @Override
    public Object business(T protocol) {
        if(protocol.getExecStatus() == ExecStatus.成功) {
            return doSuccess(protocol);
        } else {
            return doFail(protocol, protocol.getExecStatus());
        }
    }

    protected Object doFail(T protocol, ExecStatus status) {
        return null;
    }

    protected abstract Object doSuccess(T protocol);

}
