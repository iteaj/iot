package com.iteaj.network.device.server.env;

import com.iteaj.network.ProtocolType;
import com.iteaj.network.device.elfin.ElfinMessage;
import com.iteaj.network.device.elfin.ElfinMessageBody;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;

/**
 * 多环境监测设备报文
 */
public class EnvElfinMessage extends ElfinMessage {

    public EnvElfinMessage(byte[] message, String equipCode, ElfinType tradeType) {
        super(message, equipCode, tradeType);
    }

    public EnvElfinMessage(ElfinMessageHeader head, ElfinMessageBody body) {
        super(head, body);
        this.message = body.getBodyMessage();
    }

    @Override
    public EnvElfinMessage build() {
        this.messageHead = new ElfinMessageHeader(this.getEquipCode(), getTradeType());
        return this;
    }

    @Override
    public ElfinType getTradeType() {
        return (ElfinType) super.getTradeType();
    }
}
