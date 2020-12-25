package com.iteaj.iot.client.json;

import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppClientUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class AppClientProtocolDecoder extends DelimiterBasedFrameDecoder {
    public AppClientProtocolDecoder(int maxFrameLength, ByteBuf delimiter) {
        super(maxFrameLength, delimiter);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        Object decode = super.decode(ctx, buffer);
        if(null == decode) return null;

        try {
            ByteBuf byteBuf = (ByteBuf) decode;
            byte[] message = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(message);
            return AppClientUtil.buildServerResponseMessage(message);
        } finally {

        }
    }
}
