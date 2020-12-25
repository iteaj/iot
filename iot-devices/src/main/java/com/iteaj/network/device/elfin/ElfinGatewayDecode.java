package com.iteaj.network.device.elfin;

import java.util.List;

import com.iteaj.network.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

/**
 * Elfin网关设备(透传)解码器
 * 官网: http://www.hi-flying.com/
 * 产品: http://www.hi-flying.com/index.php?route=product/product/show&product_id=247
 * @author Iteaj
 *
 */
public abstract class ElfinGatewayDecode extends ByteToMessageDecoder {

    private int maxFrameLength = 256;
    private static final String DeviceSnAttr = "DeviceSn";

    public ElfinGatewayDecode() {
        this(1024);
    }

    public ElfinGatewayDecode(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        AttributeKey<String> attributeKey = AttributeKey.valueOf(DeviceSnAttr);

        byte[] message = new byte[readableBytes];
        in.readBytes(message);

        Attribute<String> attr = ctx.channel().attr(attributeKey);

        if(readableBytes == 6) { // 属于心跳包
            String deviceSn = attr.get();

            if(deviceSn == null) {
                attr.set(deviceSn = ByteUtil.bytesToHex(message));
            }

            out.add(getHeartMessage(deviceSn, message));
        } else {
            out.add(buildMessage(attr.get(), message));
        }

    }


    /**
     * 构建报文
     * @param deviceSn
     * @param message
     * @return
     */
    protected abstract ElfinMessage buildMessage(String deviceSn, byte[] message);

    /**
     * 获取心跳包报文
     * @param deviceSn
     * @param message
     * @return
     */
    protected abstract ElfinMessage getHeartMessage(String deviceSn, byte[] message);
}