package com.iteaj.network.server;

import com.iteaj.network.Protocol;
import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.server.service.DeviceRequestService;

public class ServerProtocolHandleFactory extends BusinessFactory<ServerProtocolHandle> {

    @Override
    protected Class<? extends Protocol> getProtocolClass(ServerProtocolHandle item) {
        return item.protocolClass();
    }

    @Override
    protected Class<ServerProtocolHandle> getServiceClass() {
        return ServerProtocolHandle.class;
    }
}
