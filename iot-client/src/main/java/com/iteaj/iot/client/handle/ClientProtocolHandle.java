package com.iteaj.iot.client.handle;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.ClientRequestProtocol;
import com.iteaj.network.Protocol;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.client.AbstractClientProtocol;
import com.iteaj.network.client.ClientMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

public class ClientProtocolHandle extends SimpleChannelInboundHandler<ClientMessage> {

    private ClientComponent clientComponent;
    private Logger logger = LoggerFactory.getLogger(ClientProtocolHandle.class);

    public ClientProtocolHandle(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
        if(this.clientComponent == null) {
            throw new IllegalArgumentException("ClientProtocolHandle必填参数[ClientComponent]");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) throws Exception {
        String messageId = msg.getMessageId();
        if(!StringUtils.hasText(messageId)) {
            logger.warn("messageId不存在({}) 报文: {}", clientComponent.name(), msg);
            return;
        }

        final ProtocolFactory protocolFactory = clientComponent.protocolFactory();
        if(protocolFactory == null) {
            logger.error("协议工厂不存在 组件名称: {} - 组件类型: {}", clientComponent.name()
                    , clientComponent.getClass().getSimpleName());
            return;
        }

        Protocol protocol = protocolFactory.getProtocol(msg);
        if(protocol == null) {
            logger.warn("找不到协议({}) messageId: {} - 设备编号: {} - 报文: {}", clientComponent.name(),messageId, msg.getDeviceSn(), msg);
            return;
        }

        if(protocol instanceof ClientRequestProtocol) {
            ((ClientRequestProtocol<ClientMessage>) protocol)
                    .setResponseMessage(msg).buildResponseMessage();
        } else {
            logger.error("错误的协议({}) 协议类型: {} - 说明: [{}]必须是[{}]的子类", clientComponent.name()
                    , protocol.protocolType(), protocol.getClass().getSimpleName(), AbstractClientProtocol.class.getSimpleName());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().eventLoop().schedule(()->{
            clientComponent.nettyClient().doConnect();
        }, 30, TimeUnit.SECONDS);
    }
}
