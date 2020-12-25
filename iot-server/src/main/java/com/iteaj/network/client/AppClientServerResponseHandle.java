package com.iteaj.network.client;

import com.iteaj.network.client.app.AppClientResponseBody;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import com.iteaj.network.server.service.PlatformRequestService;

public abstract class AppClientServerResponseHandle<T extends PlatformRequestProtocol> extends PlatformRequestService<T> {

    @Override
    public AppClientResponseBody business(T protocol) {
        return (AppClientResponseBody) super.business(protocol);
    }

    @Override
    protected abstract AppClientResponseBody success(T protocol);

    @Override
    protected final Object doSuccess(T protocol) {
        throw new UnsupportedOperationException("不支持的操作");
    }

    @Override
    protected abstract AppClientResponseBody failed(T protocol, ExecStatus status);

    @Override
    protected final Object doFailed(T protocol, ExecStatus status) {
        throw new UnsupportedOperationException("不支持的操作");
    }
}
