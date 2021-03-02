package com.iteaj.network.server.component;

import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.DeviceServerDecoderComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * create time: 2021/2/24
 *  基于长度字段的报文解码器组件
 * @see LengthFieldBasedFrameDecoder
 * @author iteaj
 * @since 1.0
 */
public abstract class LengthFieldBasedFrameDecoderComponentAdapter<M extends UnParseBodyMessage> extends DeviceServerDecoderComponent<M> {

    private final ByteOrder byteOrder;
    private final int maxFrameLength;
    private final int lengthFieldOffset;
    private final int lengthFieldLength;
    private final int lengthAdjustment;
    private final int initialBytesToStrip;
    private final boolean failFast;

    public LengthFieldBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        this(deviceProperties, maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
    }

    public LengthFieldBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        this(deviceProperties, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
    }

    public LengthFieldBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        this(deviceProperties, ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }/**/

    public LengthFieldBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(deviceProperties);
        this.failFast = failFast;
        this.byteOrder = byteOrder;
        this.maxFrameLength = maxFrameLength;
        this.lengthAdjustment = lengthAdjustment;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;
        this.initialBytesToStrip = initialBytesToStrip;
    }



    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return new LengthFieldBasedFrameDecoderWrapper(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    public List<M> decodes(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        throw new UnsupportedOperationException("不支持此方法, 请使用方法：DeviceMessageDecoder.decode(ctx, in)");
    }

    protected class LengthFieldBasedFrameDecoderWrapper extends LengthFieldBasedFrameDecoder {

        public LengthFieldBasedFrameDecoderWrapper(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
            super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            Object decode = super.decode(ctx, in);
            if(decode instanceof ByteBuf) {
                try {
                    M message = LengthFieldBasedFrameDecoderComponentAdapter.this.proxy(ctx, (ByteBuf) decode);

                    return message != null ? message : decode;
                } catch (Exception e) {
                    ctx.fireExceptionCaught(e);
                }
            }

            return decode;
        }
    }
}
