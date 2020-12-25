package com.iteaj.iot.client.modbus.codec;

import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteOrder;

public class ModbusClientDecoder extends LengthFieldBasedFrameDecoder {

    public ModbusClientDecoder(ByteOrder order) {
        super(order, 512, 4, 2, 0, 0, true);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decode = super.decode(ctx, in);

        if(decode instanceof ByteBuf) {
            try {
                int readableBytes = ((ByteBuf) decode).readableBytes();
                byte[] message = new byte[readableBytes];

                ((ByteBuf) decode).readBytes(message);

                ModbusStandardMessage modbusStandardMessage = ModbusStandardMessage.buildResponseMessage(message);
                return modbusStandardMessage;
            } finally {
                // 释放内存缓冲
                ReferenceCountUtil.release(decode);
            }
        } else {
            return decode;
        }
    }
}
