package com.iteaj.network.server.component;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.DeviceProtocolFactory;
import com.iteaj.network.ProtocolFactory;

public class ProtocolFactoryComponentWrapper<M extends AbstractMessage> extends ProtocolFactory<M> {

    private DeviceProtocolFactory deviceProtocolFactory;

    public ProtocolFactoryComponentWrapper(DeviceProtocolFactory deviceProtocolFactory) {
        this.deviceProtocolFactory = deviceProtocolFactory;
    }

    @Override
    public AbstractProtocol getProtocol(M message) {
        return deviceProtocolFactory.getProtocol(message);
    }
}
