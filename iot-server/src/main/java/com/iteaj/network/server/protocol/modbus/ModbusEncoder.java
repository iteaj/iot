package com.iteaj.network.server.protocol.modbus;

import com.iteaj.network.AbstractProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ModbusEncoder extends MessageToMessageEncoder<AbstractProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractProtocol msg, List<Object> out) throws Exception {

    }
}
