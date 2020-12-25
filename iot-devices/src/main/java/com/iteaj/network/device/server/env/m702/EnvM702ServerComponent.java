package com.iteaj.network.device.server.env.m702;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.device.server.DeviceServerProperties;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.IotDeviceServer;
import org.springframework.beans.factory.annotation.Autowired;

public class EnvM702ServerComponent extends DeviceServerComponent {

    @Autowired
    private DeviceServerProperties deviceServerProperties;

    @Override
    protected IotDeviceServer createDeviceServer() {
        return new EnvM702Server(deviceServerProperties.getM702());
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new EnvM702ProtocolFactory();
    }

    @Override
    public Class<? extends AbstractMessage> messageClass() {
        return EnvM702Message.class;
    }
}
