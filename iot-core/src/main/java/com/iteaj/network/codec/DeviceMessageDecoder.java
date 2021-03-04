package com.iteaj.network.codec;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.CoreConst;
import com.iteaj.network.message.UnParseBodyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.ReferenceCounted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * create time: 2021/2/21
 *
 * @author iteaj
 * @since 1.0
 */
public interface DeviceMessageDecoder<M extends AbstractMessage, R extends ReferenceCounted> {

    /**
     *
     * 对decode方法进行代理
     * @see #decode(ChannelHandlerContext, R)
     * @param ctx
     * @param in
     * @return
     */
    default M proxy(ChannelHandlerContext ctx, R in) throws Exception {
        M message = decode(ctx, in);
        if(message != null) {
            if(message instanceof UnParseBodyMessage) {
                ((UnParseBodyMessage) message).build();
            }
        }

        return message;
    }

    /**
     * 解码单条报文
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    M decode(ChannelHandlerContext ctx, R in) throws Exception;

    /**
     * 解码报文列表
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    default List<M> decodes(ChannelHandlerContext ctx, R in) throws Exception {
        M decode = proxy(ctx, in);
        if(decode != null) {
            return Arrays.asList(decode);
        } else {
            return null;
        }
    }
}
