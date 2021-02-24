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

    private LengthFieldBasedFrameDecoderWrapper decoderWrapper;

    public LengthFieldBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(deviceProperties);
        decoderWrapper = new LengthFieldBasedFrameDecoderWrapper(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    public LengthFieldBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(deviceProperties);
        decoderWrapper = new LengthFieldBasedFrameDecoderWrapper(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public LengthFieldBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(deviceProperties);
        decoderWrapper = new LengthFieldBasedFrameDecoderWrapper(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    public LengthFieldBasedFrameDecoderComponentAdapter(DeviceProperties deviceProperties, ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(deviceProperties);
        decoderWrapper = new LengthFieldBasedFrameDecoderWrapper(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }



    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return this.decoderWrapper;
    }

    @Override
    public List<M> decodes(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        throw new UnsupportedOperationException("不支持此方法, 请使用方法：DeviceMessageDecoder.decode(ctx, in)");
    }

    protected class LengthFieldBasedFrameDecoderWrapper extends LengthFieldBasedFrameDecoder {

        public LengthFieldBasedFrameDecoderWrapper(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
            super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        }

        public LengthFieldBasedFrameDecoderWrapper(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
            super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        }

        public LengthFieldBasedFrameDecoderWrapper(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
            super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
        }

        public LengthFieldBasedFrameDecoderWrapper(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
            super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            Object decode = super.decode(ctx, in);
            if(decode instanceof ByteBuf) {
                M message = LengthFieldBasedFrameDecoderComponentAdapter.this.decode(ctx, (ByteBuf) decode);

                return message != null ? message.build() : decode;
            }

            return decode;
        }
    }
}
