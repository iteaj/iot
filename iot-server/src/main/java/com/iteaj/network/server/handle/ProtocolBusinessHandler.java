package com.iteaj.network.server.handle;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.Message;
import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.client.AppClientServerProtocol;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.ServerComponentFactory;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;
import com.iteaj.network.server.protocol.NoneDealProtocol;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>协议的业务处理 handler</p>
 * Create Date By 2020-09-06
 * @author iteaj
 * @since 1.7
 */
public class ProtocolBusinessHandler extends SimpleChannelInboundHandler<UnParseBodyMessage> {

    private BusinessFactory businessFactory;//业务工厂
    private ServerComponentFactory componentFactory; //组件工厂
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ProtocolBusinessHandler(ServerComponentFactory componentFactory, BusinessFactory businessFactory) {
        this.componentFactory = componentFactory;
        this.businessFactory = businessFactory;
    }

    /**
     * 设备协议的业务处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnParseBodyMessage msg) throws Exception {

        DeviceServerComponent serverComponent = componentFactory.getByClass(msg.getClass());
        if(serverComponent == null) {
            logger.error("没有与报文类型: {} 对应的服务组件", msg.getClass());
            return;
        }

        /**
         *  1. 获取此次请求指定的协议, 通过协议工厂
         *  2. 执行协议动作
         */
        AbstractProtocol abstractProtocol = serverComponent.protocolFactory().getProtocol(msg);
        if(abstractProtocol instanceof NoneDealProtocol) return;

        if(null != abstractProtocol){

            /**
             * 如果采用同步的方案则不执行业务,业务的控制交由调用以下方法的线程：
             * @see PlatformRequestProtocol#sync(long)
             */
            if(abstractProtocol.isSyncRequest()) {
                return;
            }

            AbstractProtocol exec = abstractProtocol.exec(businessFactory);

            /**
             * 如果报文是设备主动发起请求的报文, 如果执行业务之后有返回响应报文, 则需要写出响应报文到客户端
             */
            if(exec instanceof DeviceRequestProtocol && exec.responseMessage() != null){
                final Message.MessageHead responseHead = exec.responseMessage().getHead();

                String desc = exec instanceof AppClientServerProtocol ? "平台响应应用客户端" : "平台响应设备客户端";

                ctx.writeAndFlush(exec).addListener((ChannelFutureListener) future -> {
                    String msg1 = future.isSuccess() ? "成功" : "失败";
                    if(logger.isDebugEnabled()) {
                        logger.debug("{}({}) HASH: {} - 协议类型: {} - 设备编号: {} - 响应报文: {}", desc, msg1, hashCode()
                                , responseHead.getTradeType(), responseHead.getEquipCode(), exec.responseMessage());
                    }
                });
            }
        } else {
            Message.MessageHead head = msg.getHead();
            logger.warn("描述：找不到指定协议 - 协议类型：{} - 设备编号：{} - messageId: {}"
                    , head.getTradeType(), head.getEquipCode(), head.getMessageId());
        }
    }

}
