package com.iteaj.network.event;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.springframework.context.ApplicationEvent;

/**
 * Netty 异常处理
 * @see ChannelInboundHandler#exceptionCaught(ChannelHandlerContext, Throwable)
 */
public class ExceptionEvent extends ApplicationEvent {

    private String deviceSn;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ExceptionEvent(Throwable source, String deviceSn) {
        super(source);
        this.deviceSn = deviceSn;
    }

    @Override
    public Throwable getSource() {
        return (Throwable) super.getSource();
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
}
