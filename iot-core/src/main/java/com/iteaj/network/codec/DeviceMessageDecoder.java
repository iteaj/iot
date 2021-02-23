package com.iteaj.network.codec;

import com.iteaj.network.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * create time: 2021/2/21
 *
 * @author iteaj
 * @since 1.0
 */
public interface DeviceMessageDecoder<M extends AbstractMessage> {

    M decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception;

}
