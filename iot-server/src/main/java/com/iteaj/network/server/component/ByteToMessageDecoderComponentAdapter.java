package com.iteaj.network.server.component;

import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.DeviceServerDecoderComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * create time: 2021/2/21
 *  适配{@link ByteToMessageDecoder}解码器到服务组件{@link DeviceServerComponent}
 * @author iteaj
 * @since 1.0
 */
public abstract class ByteToMessageDecoderComponentAdapter<M extends UnParseBodyMessage> extends DeviceServerDecoderComponent<M> {


    private ChannelInboundHandlerAdapter decoder = new ByteToMessageDecoderWrapper();

    public ByteToMessageDecoderComponentAdapter(DeviceProperties deviceProperties) {
        super(deviceProperties);
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return this.decoder;
    }

    protected class ByteToMessageDecoderWrapper extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            M decode = ByteToMessageDecoderComponentAdapter.this.decode(ctx, in);
            if(decode != null) {
                out.add(decode.build());
            }
        }
    }
}
