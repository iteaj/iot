package com.iteaj.network.device.server.pdu;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;
import org.apache.commons.lang3.StringUtils;

public class PduProtocolDecoder extends LineBasedFrameDecoder {

    private String equipCode; // 设备编号
    public PduProtocolDecoder(int maxLength) {
        super(maxLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        Object decode = super.decode(ctx, buffer);
        if(decode != null && decode instanceof ByteBuf) {
            PduMessage message = new PduMessage((ByteBuf) decode).build();
            PduMessage.PduMessageHead head = (PduMessage.PduMessageHead)message.getHead();
            if(StringUtils.isNotBlank(head.getEquipCode())) {
                this.equipCode = head.getEquipCode();
            } else {
                head.setEquipCode(this.equipCode);
                if(this.equipCode == null) return decode;
            }

            return message;
        }

        return decode;
    }
}
