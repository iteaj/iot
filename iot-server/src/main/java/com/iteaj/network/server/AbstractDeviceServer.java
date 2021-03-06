package com.iteaj.network.server;

import com.iteaj.network.ProtocolException;
import com.iteaj.network.config.DeviceProperties;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

public abstract class AbstractDeviceServer implements IotDeviceServer {

    private int port;
    private DeviceProperties config;
    public AbstractDeviceServer(int port) {
        this(new DeviceProperties(port));
        this.port = port;
    }

    public AbstractDeviceServer(DeviceProperties serverConfig) {
        this.config = serverConfig;
        this.port = serverConfig.getPort();
    }

    @Override
    public int port() {
        return this.port;
    }

    /**
     * 按照开启的端口进行匹配
     * @param ch
     * @return
     */
    public boolean isMatcher(SocketChannel ch, int port) {
        return port == port();
    }

    @Override
    public void initChannelPipeline(ChannelPipeline p) {
        ChannelInboundHandlerAdapter adapter = getMessageDecoder();

        if(adapter == null) {
            throw new ProtocolException("未指定设备报文解码器");
        }

        p.addFirst("decoder", adapter);

        // 有一个值设定, 就启用
        if(config.getReaderIdleTime() > 0 || config.getAllIdleTime() > 0
                || config.getWriterIdleTime() > 0) {
            p.addLast("idleState", new IdleStateHandler(config.getReaderIdleTime()
                    , config.getWriterIdleTime(), config.getAllIdleTime(), TimeUnit.SECONDS));
        }
    }

}
