package com.iteaj.network.device.server.env;

import com.iteaj.network.device.server.DeviceServerProperties;
import com.iteaj.network.server.AbstractDeviceServer;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 多功能环境监测设备Netty服务
 */
public class EnvDeviceServer extends AbstractDeviceServer {

    public EnvDeviceServer(DeviceServerProperties.ServerConfig serverConfig) {
        super(serverConfig);
    }

    @Override
    protected ChannelInboundHandlerAdapter getMessageDecoder() {
        return new EnvElfinDecoder();
    }

    @Override
    public String name() {
        return "十七合一环境采集设备";
    }

    @Override
    public String desc() {
        return "采样值有(so2, no2, co, co2, o3, pm2.5, pm10, 温度, 湿度, 气压, 风速, 风向, 雨量, 辐射, 光照, 紫外辐射, 噪音); 使用Elfin-EE11A网关(心跳包格式: %HMAC)";
    }
}
