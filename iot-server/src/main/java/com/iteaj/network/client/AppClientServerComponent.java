package com.iteaj.network.client;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.IotDeviceServer;
import com.iteaj.network.server.IotServerProperties;
import org.springframework.beans.factory.annotation.Autowired;

public class AppClientServerComponent extends DeviceServerComponent {

    @Autowired
    private IotServerProperties properties;

    @Override
    protected IotDeviceServer createDeviceServer() {
        return new AppClientServer(this.properties.getClientPort());
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new AppServerProtocolFactory(protocolTimeoutStorage());
    }

    @Override
    public Class<? extends AbstractMessage> messageClass() {
        return AppClientMessage.class;
    }

}
