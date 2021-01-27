package com.iteaj.network.device.server.gps.protocol;

import com.iteaj.network.device.server.gps.GpsCommonRespResult;
import com.iteaj.network.device.server.gps.GpsMessage;
import com.iteaj.network.device.server.gps.GpsProtocolType;

/**
 * create time: 2021/1/22
 *  终端心跳协议
 * @author iteaj
 * @since 1.0
 */
public class HeartProtocol extends GpsDeviceRequestProtocol{

    public HeartProtocol(GpsMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected GpsMessage doBuildResponseMessage() {
        return GpsMessage.buildPlatformCommonRespMessageByRequest(requestMessage(), GpsCommonRespResult.成功);
    }

    @Override
    protected void resolverRequestMessage(GpsMessage requestMessage) {

    }

    @Override
    public GpsProtocolType protocolType() {
        return GpsProtocolType.Heart;
    }
}
