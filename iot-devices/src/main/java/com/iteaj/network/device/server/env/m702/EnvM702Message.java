package com.iteaj.network.device.server.env.m702;

import com.iteaj.network.device.elfin.ElfinMessage;
import com.iteaj.network.device.elfin.ElfinMessageBody;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;

public class EnvM702Message extends ElfinMessage {

    public EnvM702Message(byte[] message, String equipCode, ElfinType tradeType) {
        super(message, equipCode, tradeType);
    }

    public EnvM702Message(ElfinMessageHeader head, ElfinMessageBody body) {
        super(head, body);
        this.setMessage(body.getBodyMessage());
    }

    @Override
    public EnvM702Message build(){
        this.messageHead = new ElfinMessageHeader(this.getEquipCode(), getTradeType());
        return this;
    }
}
