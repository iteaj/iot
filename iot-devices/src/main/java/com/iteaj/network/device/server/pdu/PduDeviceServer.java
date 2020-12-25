package com.iteaj.network.device.server.pdu;

import com.iteaj.network.device.server.DeviceServerProperties;
import com.iteaj.network.server.AbstractDeviceServer;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PduDeviceServer extends AbstractDeviceServer {

    public PduDeviceServer(DeviceServerProperties.ServerConfig serverConfig) {
        super(serverConfig);
    }

    @Override
    public String name() {
        return "智慧融合控制台";
    }

    @Override
    public String desc() {
        return "用于监听智慧融合控制台智能插座连接请求";
    }

    @Override
    protected ChannelInboundHandlerAdapter getMessageDecoder() {
        return new PduProtocolDecoder(1024);
    }
}
