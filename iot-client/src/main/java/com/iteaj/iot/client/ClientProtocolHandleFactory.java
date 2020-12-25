package com.iteaj.iot.client;

import com.iteaj.network.Protocol;
import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.client.ClientProtocolHandle;

public class ClientProtocolHandleFactory extends BusinessFactory<ClientProtocolHandle> {

    @Override
    protected Class<? extends Protocol> getProtocolClass(ClientProtocolHandle item) {
        return item.protocolClass();
    }

    @Override
    protected Class<ClientProtocolHandle> getServiceClass() {
        return ClientProtocolHandle.class;
    }
}
