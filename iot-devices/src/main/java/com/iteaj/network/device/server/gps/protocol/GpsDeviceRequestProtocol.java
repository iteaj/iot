package com.iteaj.network.device.server.gps.protocol;

import com.iteaj.network.device.server.gps.GpsMessage;
import com.iteaj.network.device.server.gps.GpsProtocolType;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;

/**
 * create time: 2021/1/20
 *  Gps设备主动请求平台的协议请求
 * @author iteaj
 * @since 1.0
 */
public abstract class GpsDeviceRequestProtocol extends DeviceRequestProtocol<GpsMessage> {

    public GpsDeviceRequestProtocol(GpsMessage requestMessage) {
        super(requestMessage);
        setEquipCode(requestMessage.getHead().getEquipCode());
    }


    @Override
    protected abstract GpsMessage doBuildResponseMessage();

    @Override
    protected abstract void resolverRequestMessage(GpsMessage requestMessage);

    @Override
    public abstract GpsProtocolType protocolType();
}
