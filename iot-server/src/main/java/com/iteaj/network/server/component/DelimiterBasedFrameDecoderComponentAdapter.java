package com.iteaj.network.server.component;

import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.DeviceServerDecoderComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.util.List;

/**
 * create time: 2021/2/22
 *  适配{@link DelimiterBasedFrameDecoder}解码器到服务组件{@link DeviceServerComponent}
 * @author iteaj
 * @since 1.0
 */
public abstract class DelimiterBasedFrameDecoderComponentAdapter<M extends UnParseBodyMessage> extends DeviceServerDecoderComponent<M> {

    private final ByteBuf[] delimiters;
    private final int maxFrameLength;
    private final boolean stripDelimiter;
    private final boolean failFast;

    public DelimiterBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, ByteBuf delimiter) {
        this(deviceProperties, maxFrameLength, true, delimiter);
    }

    public DelimiterBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, boolean stripDelimiter, ByteBuf delimiter) {
        this(deviceProperties, maxFrameLength, stripDelimiter, true, delimiter);
    }

    public DelimiterBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf delimiter) {
        super(deviceProperties);
        this.maxFrameLength = maxFrameLength;
        this.delimiters = new ByteBuf[]{delimiter};
        this.stripDelimiter = stripDelimiter;
        this.failFast = failFast;
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return new DelimiterBasedFrameDecoderWrapper(maxFrameLength, stripDelimiter, failFast, this.delimiters);
    }


    @Override
    public List<M> decodes(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        throw new UnsupportedOperationException("不支持此方法, 请使用方法：DeviceMessageDecoder.decode(ctx, in)");
    }

    protected class DelimiterBasedFrameDecoderWrapper extends DelimiterBasedFrameDecoder {

        public DelimiterBasedFrameDecoderWrapper(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf... delimiters) {
            super(maxFrameLength, stripDelimiter, failFast, delimiters);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            Object decode = super.decode(ctx, buffer);
            if(decode instanceof ByteBuf) {
                try {
                    M message = DelimiterBasedFrameDecoderComponentAdapter.this.decode(ctx, (ByteBuf) decode);

                    return message != null ? message.build() : decode;
                } catch (Exception e) {
                    ctx.fireExceptionCaught(e);
                }
            }

            return decode;
        }
    }
}
