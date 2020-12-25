package com.iteaj.iot.client.modbus.codec;

import com.iteaj.iot.client.modbus.protocol.ModbusStandardProtocol;
import com.iteaj.network.utils.ByteUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ModbusClientEncoder extends MessageToMessageEncoder<ModbusStandardProtocol> {

    private static Logger logger = LoggerFactory.getLogger(ModbusStandardProtocol.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ModbusStandardProtocol msg, List<Object> out) throws Exception {
        out.add(Unpooled.wrappedBuffer(msg.requestMessage().getMessage()));
        logger.info("发送Modbus报文 - 报文: {}", ByteUtil.bytesToHex(msg.requestMessage().getMessage()));
    }
}
