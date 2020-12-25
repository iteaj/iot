package com.iteaj.network.device.server.env.m702;

import com.iteaj.network.device.server.DeviceServerProperties;
import com.iteaj.network.server.AbstractDeviceServer;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 7合一环境监测服务端组件
 */
public class EnvM702Server extends AbstractDeviceServer {

    public EnvM702Server(DeviceServerProperties.ServerConfig serverConfig) {
        super(serverConfig);
    }

    @Override
    protected ChannelInboundHandlerAdapter getMessageDecoder() {
        return new EnvM702Decoder();
    }

    @Override
    public String name() {
        return "M702(七合一)环境监测";
    }

    @Override
    public String desc() {
        return "七合一环境采样监测(co2, tvoc, jiaquan, pm2.5, pm10, wenDu, shiDu); 使用Elfin-EE11A网关(心跳包格式: %HMAC)";
    }
}
