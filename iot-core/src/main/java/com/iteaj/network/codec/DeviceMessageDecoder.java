package com.iteaj.network.codec;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.message.UnParseBodyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * create time: 2021/2/21
 *
 * @author iteaj
 * @since 1.0
 */
public interface DeviceMessageDecoder<M extends AbstractMessage> {

    /**
     * 解码单条报文
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    M decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception;

    /**
     * 解码报文列表
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    default List<M> decodes(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        M decode = decode(ctx, in);
        if(decode != null) {
            List<M> out = new ArrayList<>();

            if(decode instanceof UnParseBodyMessage) {
                ((UnParseBodyMessage) decode).build();
            }

            out.add(decode);

            return out;
        } else {
            return null;
        }
    }
}
