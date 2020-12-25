package com.iteaj.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 设备服务端, 一款设备一般对应一个服务端口, 支持多设备接入
 */
public interface IotDeviceServer {

    /**
     * 服务端名称
     * @return
     */
    String name();

    /**
     * 设备服务说明
     * @return
     */
    String desc();

    /**
     * 监听的端口
     * @return
     */
    int port();

    /**
     * 开启监听端口并且绑定
     * @param sb
     */
    void doBind(ServerBootstrap sb, ApplicationContext context);

    /**
     * 当前链接的Channel是否匹配此设备服务端
     * @param ch
     * @return
     */
    boolean isMatcher(SocketChannel ch, int port);

    /**
     * @see #isMatcher(SocketChannel, int) 如果返回true 将调用此方法进行channel初始化
     * @param pipeline
     */
    void initChannelPipeline(ChannelPipeline pipeline);

    Logger LOGGER = LoggerFactory.getLogger(IotDeviceServer.class);
}
