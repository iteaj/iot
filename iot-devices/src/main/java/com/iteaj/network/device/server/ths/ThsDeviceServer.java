package com.iteaj.network.device.server.ths;

import com.iteaj.network.server.AbstractDeviceServer;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ThsDeviceServer extends AbstractDeviceServer {

    public ThsDeviceServer(int port) {
        super(port);
    }

    @Override
    public String name() {
        return "空调温湿度设备";
    }

    @Override
    public String desc() {
        return "红外空调控制, 可携带传感器(人体感应, 烟雾探测, 水侵等开关量设备); 使用Elfin-EE11A网关(心跳包格式: %HMAC)";
    }

    @Override
    protected ChannelInboundHandlerAdapter getMessageDecoder() {
        return new THSElfinDecoder();
    }
}
