package com.iteaj.iot.client;

import com.iteaj.iot.client.codec.ClientRequestEncoder;
import com.iteaj.iot.client.handle.ClientProtocolHandle;
import com.iteaj.iot.client.mqtt.common.NettyLog;
import com.iteaj.iot.client.mqtt.protocol.ClientProtocolUtil;
import com.iteaj.network.ProtocolException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.concurrent.TimeUnit;

public abstract class IotNettyClient implements IotClient{

    private long delay = 60;
    private Channel channel;
    private Bootstrap bootstrap;
    private ClientComponent clientComponent;

    protected static Logger logger = LoggerFactory.getLogger(IotNettyClient.class);

    public IotNettyClient(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

    public void init(NioEventLoopGroup clientGroup) {
        this.bootstrap = new Bootstrap().group(clientGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(getHost(), getPort());

        this.doInit(this.bootstrap).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                IotNettyClient.this.channel = channel;

                IotNettyClient.this.doInitChannel(channel);
            }
        });
    }

    protected Bootstrap doInit(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        return bootstrap;
    }

    public void doConnect() {
        try {
            this.bootstrap.connect().addListener(future -> {
                ChannelFuture channelFuture = (ChannelFuture) future;
                if (future.isSuccess()) {
                    logger.info("客户端({}) 连接服务器成功 - 远程主机 {}:{}"
                            , getClientComponent().name(), this.getHost(), this.getPort());
                    IotNettyClient.this.successCallback(channelFuture);
                } else {
                    Throwable cause = future.cause();
                    logger.error("客户端({}) 连接服务器失败, 60秒后重新连接 - 远程主机 {}:{}"
                            , getClientComponent().name(), this.getHost(), this.getPort(), cause);

                    channelFuture.channel().eventLoop().schedule(()->{
                        logger.error("客户端({}) 客户端再次尝试重连 - 主机 {}:{}"
                                , getClientComponent().name(), this.getHost(), this.getPort());
                        doConnect();
                    }, delay, TimeUnit.SECONDS);
                }
            });
        } catch (Exception e) {
            logger.error("客户端() 连接异常 - 远程主机: {}:{}", getClientComponent().name(), this.getHost(), this.getPort(), e);
        }
    }

    /**
     * 同步链接
     * @see #successCallback(ChannelFuture) 将不会被调用
     * @param timeout
     * @return
     */
    public void doConnect(long timeout) throws ProtocolException {
        try {
            boolean await = this.getBootstrap().connect().sync().await(timeout, TimeUnit.SECONDS);
            if(!await) throw new ProtocolException("链接超时("+getClientComponent().name()+") - " + getHost() + ":" + getPort());

            logger.info("客户端({}) 连接服务器成功 - 远程主机: {}:{}", getClientComponent().name(), getHost(), getPort());
        } catch (InterruptedException e) {
            throw new ProtocolException("链接中断("+getClientComponent().name()+") - " + getHost() + ":" + getPort(), e);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if(cause instanceof ConnectException) {
                throw new ProtocolException("服务端拒绝连接", cause);
            } else if(cause instanceof NoRouteToHostException) {
                throw new ProtocolException(e.getMessage(), cause);
            } else {
                throw new ProtocolException("连接服务器失败", e);
            }
        }
    }

    public ChannelFuture writeAndFlush(ClientRequestProtocol clientRequestProtocol) {
        return this.channel.writeAndFlush(clientRequestProtocol);
    }

    public void successCallback(ChannelFuture future) { }

    protected void doInitChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addFirst("ClientRequestDecoder", initClientRequestDecoder());
        pipeline.addFirst(ClientRequestEncoder.class.getSimpleName(), new ClientRequestEncoder(getClientComponent()));

        pipeline.addLast(new ClientProtocolHandle(getClientComponent()));
    }

    protected abstract ChannelInboundHandler initClientRequestDecoder();

    public ClientComponent getClientComponent() {
        return clientComponent;
    }

    public void setClientComponent(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

    public String getName() {
        return this.clientComponent.name();
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
