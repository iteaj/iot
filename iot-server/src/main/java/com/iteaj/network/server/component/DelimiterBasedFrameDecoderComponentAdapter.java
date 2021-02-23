package com.iteaj.network.server.component;

import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.DeviceServerDecoderComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * create time: 2021/2/22
 *  适配{@link DelimiterBasedFrameDecoder}解码器到服务组件{@link DeviceServerComponent}
 * @author iteaj
 * @since 1.0
 */
public abstract class DelimiterBasedFrameDecoderComponentAdapter<M extends UnParseBodyMessage> extends DeviceServerDecoderComponent<M> {


    private DelimiterBasedFrameDecoderWrapper decoderWrapper;

    public DelimiterBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, ByteBuf delimiter) {
        super(deviceProperties);
        this.decoderWrapper = new DelimiterBasedFrameDecoderWrapper(maxFrameLength, delimiter);
    }

    public DelimiterBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, boolean stripDelimiter, ByteBuf delimiter) {
        super(deviceProperties);
        this.decoderWrapper = new DelimiterBasedFrameDecoderWrapper(maxFrameLength, stripDelimiter, delimiter);
    }

    public DelimiterBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf delimiter) {
        super(deviceProperties);
        this.decoderWrapper = new DelimiterBasedFrameDecoderWrapper(maxFrameLength, stripDelimiter, failFast, delimiter);
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return this.decoderWrapper;
    }

    protected class DelimiterBasedFrameDecoderWrapper extends DelimiterBasedFrameDecoder {

        public DelimiterBasedFrameDecoderWrapper(int maxFrameLength, ByteBuf delimiter) {
            super(maxFrameLength, delimiter);
        }

        public DelimiterBasedFrameDecoderWrapper(int maxFrameLength, boolean stripDelimiter, ByteBuf delimiter) {
            super(maxFrameLength, stripDelimiter, delimiter);
        }

        public DelimiterBasedFrameDecoderWrapper(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf delimiter) {
            super(maxFrameLength, stripDelimiter, failFast, delimiter);
        }

        public DelimiterBasedFrameDecoderWrapper(int maxFrameLength, ByteBuf... delimiters) {
            super(maxFrameLength, delimiters);
        }

        public DelimiterBasedFrameDecoderWrapper(int maxFrameLength, boolean stripDelimiter, ByteBuf... delimiters) {
            super(maxFrameLength, stripDelimiter, delimiters);
        }

        public DelimiterBasedFrameDecoderWrapper(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf... delimiters) {
            super(maxFrameLength, stripDelimiter, failFast, delimiters);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            Object decode = super.decode(ctx, buffer);
            if(decode instanceof ByteBuf) {
                return DelimiterBasedFrameDecoderComponentAdapter.this.decode(ctx, (ByteBuf) decode);
            } else {
                return decode;
            }
        }
    }
}
