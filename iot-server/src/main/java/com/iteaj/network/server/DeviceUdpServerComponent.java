package com.iteaj.network.server;

import com.iteaj.network.ProtocolTimeoutStorage;

public abstract class DeviceUdpServerComponent extends ServerComponent {

    /**
     * Udp协议一般不需要进行同步调用, 去除协议的超时存储
     * @return
     */
    @Override
    protected ProtocolTimeoutStorage doCreateProtocolTimeoutStorage() {
        return null;
    }
}
