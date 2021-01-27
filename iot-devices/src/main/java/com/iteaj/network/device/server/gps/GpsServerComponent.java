package com.iteaj.network.device.server.gps;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.device.server.DeviceServerProperties;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.IotDeviceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * create time: 2021/1/14
 *
 * @author iteaj
 * @since 1.0
 */
@Component
public class GpsServerComponent extends DeviceServerComponent {

    @Autowired
    private DeviceServerProperties properties;

    @Override
    protected IotDeviceServer createDeviceServer() {
        return new GpsDeviceServer(properties.getGps());
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new GpsProtocolFactory();
    }

    @Override
    public Class<? extends AbstractMessage> messageClass() {
        return GpsMessage.class;
    }
}
