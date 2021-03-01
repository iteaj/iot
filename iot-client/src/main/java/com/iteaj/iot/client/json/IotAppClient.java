package com.iteaj.iot.client.json;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.network.client.app.AppClientUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;

import java.util.concurrent.TimeUnit;

public class IotAppClient extends IotNettyClient {

    private int port;
    private String host;

    public IotAppClient(String host, int port, ClientComponent clientComponent) {
        super(clientComponent);
        this.host = host;
        this.port = port;
    }

    @Override
    protected ChannelInboundHandler initClientRequestDecoder() {
        return new AppClientProtocolDecoder();
    }

    @Override
    public void successCallback(ChannelFuture future) {
        // 每隔30秒发送一次心跳
        future.channel().eventLoop().scheduleWithFixedDelay(()->{
            AppClientProtocol.buildRequestProtocol(AppClientUtil.getHeartMessage(null)).request();
        }, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getHost() {
        return this.host;
    }
}
