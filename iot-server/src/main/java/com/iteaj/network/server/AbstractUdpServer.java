package com.iteaj.network.server;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

public abstract class AbstractUdpServer implements IotDeviceServer {

    private int port;

    public AbstractUdpServer(int port) {
        this.port = port;
    }

    @Override
    public int port() {
        return this.port;
    }

    @Override
    public void initChannelPipeline(ChannelPipeline pipeline) {
        pipeline.addFirst(getMessageDecoder());
    }

    /**
     * 返回设备解码器
     * @return
     */
    protected abstract ChannelInboundHandlerAdapter getMessageDecoder();
}
