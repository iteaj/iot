package com.iteaj.network.client;

import com.iteaj.network.CoreConst;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppClientUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

public class AppClientProtocolDecoder extends DelimiterBasedFrameDecoder {

    public AppClientProtocolDecoder() {
        super(1024, Unpooled.copiedBuffer(CoreConst.DELIMITER.getBytes()));
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        Object decode = super.decode(ctx, buffer);

        if(decode != null && decode instanceof ByteBuf) {
            InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
            String equipCode = socketAddress.getHostName()+":"+socketAddress.getPort();

            byte[] message = new byte[((ByteBuf) decode).readableBytes()];
            ((ByteBuf) decode).readBytes(message);

            return AppClientUtil.buildClientRequestMessage(message, equipCode);
        }

        return decode;
    }
}
