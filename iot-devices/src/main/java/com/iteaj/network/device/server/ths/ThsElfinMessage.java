package com.iteaj.network.device.server.ths;

import com.iteaj.network.ProtocolType;
import com.iteaj.network.device.elfin.ElfinMessage;
import com.iteaj.network.device.elfin.ElfinMessageBody;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;

public class ThsElfinMessage extends ElfinMessage {

    public ThsElfinMessage(byte[] message, String equipCode, ElfinType tradeType) {
        super(message, equipCode, tradeType);
    }

    public ThsElfinMessage(byte[] message, ElfinMessageHeader head) {
        super(head, null);
        setMessage(message);
    }

    public ThsElfinMessage(ElfinMessageHeader head, ElfinMessageBody body) {
        super(head, body);
//        this.equipCode = head.getEquipCode();
//        this.tradeType = head.getTradeType();
        this.message = body.getBodyMessage();
    }

    @Override
    public ThsElfinMessage build() {
        this.messageHead = new ElfinMessageHeader(this.getEquipCode(), getTradeType());
        return this;
    }

    @Override
    public ElfinType getTradeType() {
        return (ElfinType) super.getTradeType();
    }
}
