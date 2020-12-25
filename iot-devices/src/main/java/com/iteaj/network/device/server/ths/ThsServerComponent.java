package com.iteaj.network.device.server.ths;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.device.server.DeviceServerProperties;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.IotDeviceServer;
import org.springframework.beans.factory.annotation.Autowired;

public class ThsServerComponent extends DeviceServerComponent {

    @Autowired
    private DeviceServerProperties deviceServerProperties;

    @Override
    protected IotDeviceServer createDeviceServer() {
        DeviceProperties ths = deviceServerProperties.getThs();
        return new ThsDeviceServer(ths.getPort());
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new ThsProtocolFactory(protocolTimeoutStorage());
    }

    @Override
    public Class<? extends AbstractMessage> messageClass() {
        return ThsElfinMessage.class;
    }
}
