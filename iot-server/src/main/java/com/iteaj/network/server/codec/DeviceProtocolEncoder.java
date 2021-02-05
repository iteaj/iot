package com.iteaj.network.server.codec;

import com.iteaj.network.*;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.ServerComponentFactory;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Create Date By 2017-09-08
 *
 * @author iteaj
 * @since 1.7
 */
@ChannelHandler.Sharable
public class DeviceProtocolEncoder extends MessageToMessageEncoder<AbstractProtocol> {

    /**
     * 存储消息id和协议的映射关系
     * @see Protocol 协议
     *
     */
    private ServerComponentFactory componentFactory;

    public DeviceProtocolEncoder(ServerComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractProtocol protocol, List<Object> out) throws Exception {

        try {
            //平台主动请求的则写出请求报文
            if(protocol instanceof PlatformRequestProtocol) {
                AbstractMessage requestMessage = (AbstractMessage)protocol.requestMessage();

                //如果需要保存映射关系,用relationKey作为关联映射
                if(protocol.isRelation() && requestMessage != null){
                    DeviceServerComponent serverComponent = componentFactory.getByClass(requestMessage.getClass());
                    serverComponent.protocolTimeoutStorage().add((String)protocol.relationKey()
                            , protocol, ((PlatformRequestProtocol) protocol).getTimeout());
                }

                out.add(Unpooled.wrappedBuffer(requestMessage.getMessage()));

                //如果是设备请求的则写出响应报文
            } else if(protocol instanceof DeviceRequestProtocol){

                Message message = protocol.responseMessage();
                out.add(Unpooled.wrappedBuffer(message.getMessage()));
            } else if(protocol instanceof CommonWriteProtocol){
                Message message = protocol.responseMessage();
                out.add(Unpooled.wrappedBuffer(message.getMessage()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }

    }

}
