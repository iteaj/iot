package com.iteaj.network.device.server.gps;

import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.server.AbstractDeviceServer;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * create time: 2021/1/14
 *
 * @author iteaj
 * @since 1.0
 */
public class GpsDeviceServer extends AbstractDeviceServer {

    public GpsDeviceServer(DeviceProperties serverConfig) {
        super(serverConfig);
    }

    @Override
    protected ChannelInboundHandlerAdapter getMessageDecoder() {
        return new GpsMessageDecoder();
    }

    @Override
    public String name() {
        return "Gps定位设备";
    }

    @Override
    public String desc() {
        return "道路运输车辆主动安全智能防控系统";
    }
}
