package com.iteaj.network.client;

import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.server.AbstractDeviceServer;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 一个可以接收应用客户端链接的服务端
 */
public class AppClientServer extends AbstractDeviceServer {

    public AppClientServer(int port) {
        super(port);
    }

    public AppClientServer(DeviceProperties serverConfig) {
        super(serverConfig);
    }

    @Override
    public String name() {
        return "应用程序客户端";
    }

    @Override
    public String desc() {
        return "用于监听应用客户端连接请求";
    }

    @Override
    protected ByteToMessageDecoder getMessageDecoder() {
        return new AppClientProtocolDecoder();
    }
}
