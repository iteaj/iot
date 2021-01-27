package com.iteaj.network.device.server.gps;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * create time: 2021/1/14
 *
 * @author iteaj
 * @since 1.0
 */
public class GpsMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readableBytes = byteBuf.readableBytes();
        if(readableBytes > 0) {
            list.add(new GpsMessage(byteBuf).build());
        }
    }
}
