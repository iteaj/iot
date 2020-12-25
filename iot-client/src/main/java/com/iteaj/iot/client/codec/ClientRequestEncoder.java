package com.iteaj.iot.client.codec;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.ClientRequestProtocol;
import com.iteaj.network.ProtocolException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ClientRequestEncoder extends MessageToMessageEncoder<ClientRequestProtocol> {

    private ClientComponent clientComponent;

    public ClientRequestEncoder(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ClientRequestProtocol protocol, List<Object> out) throws Exception {
        if(protocol.isRelation()) {
            if(protocol.relationKey() == null) {
                throw new ProtocolException("不存在关联Key, relationKey()无返回值");
            }

            clientComponent.protocolTimeoutStorage().add(protocol.relationKey(), protocol, protocol.getTimeout());
        }

        out.add(Unpooled.wrappedBuffer(protocol.requestMessage().getMessage()));
    }
}
