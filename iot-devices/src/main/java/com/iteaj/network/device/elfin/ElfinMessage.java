package com.iteaj.network.device.elfin;

import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.utils.ByteUtil;

import java.io.IOException;

public abstract class ElfinMessage extends UnParseBodyMessage {

    private String equipCode;
    private ElfinType tradeType;

    public ElfinMessage(byte[] message, String equipCode, ElfinType tradeType) {
        super(message);
        this.equipCode = equipCode;
        this.tradeType = tradeType;
    }

    public ElfinMessage(ElfinMessageHeader head, ElfinMessageBody body) {
        super(head, body);
    }

    @Override
    public abstract ElfinMessage build() throws IOException;

    public String getEquipCode() {
        return equipCode;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    public ElfinType getTradeType() {
        return tradeType;
    }

    public void setTradeType(ElfinType tradeType) {
        this.tradeType = tradeType;
    }

    @Override
    public String toString() {
        return "EnvMessage{" +
                "equipCode='" + equipCode + '\'' +
                ", tradeType=" + tradeType +
                ", message=" + ByteUtil.bytesToHex(message) +
                '}';
    }
}
