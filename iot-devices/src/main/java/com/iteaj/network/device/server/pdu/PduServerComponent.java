package com.iteaj.network.device.server.pdu;

import com.iteaj.network.device.server.DeviceServerProperties;
import com.iteaj.network.AbstractMessage;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.IotDeviceServer;
import org.springframework.beans.factory.annotation.Autowired;

public class PduServerComponent extends DeviceServerComponent {

    @Autowired
    private DeviceServerProperties properties;

    @Override
    protected IotDeviceServer createDeviceServer() {
        return new PduDeviceServer(properties.getPdu());
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new PduServerProtocolFactory(protocolTimeoutStorage());
    }

    @Override
    public Class<? extends AbstractMessage> messageClass() {
        return PduMessage.class;
    }
}
