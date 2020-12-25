package com.iteaj.network.device.elfin;

import com.iteaj.network.Message;

public class ElfinMessageHeader implements Message.MessageHead {
    protected String equipCode;
    private ElfinType envTradeType;

    public ElfinMessageHeader(String equipCode, ElfinType tradeType) {
        this.equipCode = equipCode;
        this.envTradeType = tradeType;
    }

    @Override
    public String getEquipCode() {
        return this.equipCode;
    }

    @Override
    public String getMessageId() {
        if(envTradeType == ElfinType.Air_Unknow) {
            return getEquipCode() + "_Air";
        } else {
            return getEquipCode();
        }
    }

    @Override
    public ElfinType getTradeType() {
        return this.envTradeType;
    }

    @Override
    public int getHeadLength() {
        return 0;
    }

    @Override
    public byte[] getHeadMessage() {
        return null;
    }
}
