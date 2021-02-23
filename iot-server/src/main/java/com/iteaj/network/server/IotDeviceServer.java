package com.iteaj.network.server;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

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
    default void doBind(AbstractBootstrap sb, ApplicationContext context) {
        // 绑定此设备要开启的端口
        sb.bind(this.port()).addListener(future -> {
            if(future.isSuccess()) {
                LOGGER.info("监听端口成功: {} - 设备: {} - 对应组件:{} - 说明: {}"
                        , this.port(), name(), this.getClass().getSimpleName(), desc());
            } else {
                LOGGER.error("开启端口失败: {} - 设备: {} - 对应组件:{} - 说明: {} - 失败原因: {}", this.port(), name()
                        , this.getClass().getSimpleName(), desc(), future.cause().getMessage(), future.cause());

                Throwable cause = future.cause();
                if(context instanceof ConfigurableApplicationContext) {
                    LOGGER.warn("开启端口失败: {}, 将关闭Spring Application", this.port(), cause);
                    if(((ConfigurableApplicationContext) context).isActive()) {
                        ((ConfigurableApplicationContext) context).close();
                        LOGGER.warn("关闭Spring Application: {} - 状态: 关闭完成", context.getApplicationName());
                    }
                }
            }
        });
    }

    /**
     * @param pipeline
     */
    void initChannelPipeline(ChannelPipeline pipeline);

    /**
     * 返回设备解码器
     * @return
     */
    ChannelInboundHandlerAdapter getMessageDecoder();

    Logger LOGGER = LoggerFactory.getLogger(IotDeviceServer.class);
}
