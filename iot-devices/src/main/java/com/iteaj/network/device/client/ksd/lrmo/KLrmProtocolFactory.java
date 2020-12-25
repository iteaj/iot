package com.iteaj.network.device.client.ksd.lrmo;

import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.ProtocolFactory;

public class KLrmProtocolFactory extends ProtocolFactory<ModbusStandardMessage> {

    @Override
    public AbstractProtocol getProtocol(ModbusStandardMessage message) {

        return remove(message.getMessageId());
    }
}
