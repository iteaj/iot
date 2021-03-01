package com.iteaj.iot.client.json;

import com.iteaj.network.client.app.AppClientUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class AppClientProtocolDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 最大的报文2M, 0开始作为长度字段的偏移, 长度字段长4个字节
     */
    public AppClientProtocolDecoder() {
        super(1024 * 2048, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        Object decode = super.decode(ctx, buffer);
        if(decode instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) decode;
            byte[] message = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(message);

            return AppClientUtil.buildServerResponseMessage(message);
        }

        return decode;

    }
}
