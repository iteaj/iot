package com.iteaj.network.device.server.env;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.device.server.DeviceServerProperties;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.IotDeviceServer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 多环境监测设备服务端组件
 */
public class EnvServerComponent  extends DeviceServerComponent {

    @Autowired
    private DeviceServerProperties properties;

    @Override
    protected IotDeviceServer createDeviceServer() {
        return new EnvDeviceServer(properties.getEnv());
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new EnvProtocolFactory();
    }

    @Override
    public Class<? extends AbstractMessage> messageClass() {
        return EnvElfinMessage.class;
    }
}
