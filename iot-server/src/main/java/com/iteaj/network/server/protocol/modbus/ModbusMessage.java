package com.iteaj.network.server.protocol.modbus;

import com.iteaj.network.message.UnParseBodyMessage;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class ModbusMessage extends UnParseBodyMessage {

    public ModbusMessage(byte[] message) {
        super(message);
    }

    public ModbusMessage(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public UnParseBodyMessage build() throws IOException {
        return null;
    }
}
