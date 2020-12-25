package com.iteaj.network.client;

import com.iteaj.network.Message;
import com.iteaj.network.client.app.AppClientType;

public class AppClientServerHead implements Message.MessageHead {

    private String equipCode;
    private String messageId;
    private AppClientType tradeType;

    public AppClientServerHead(String equipCode, String messageId, AppClientType tradeType) {
        this.equipCode = equipCode;
        this.messageId = messageId;
        this.tradeType = tradeType;
    }

    @Override
    public String getEquipCode() {
        return equipCode;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public AppClientType getTradeType() {
        return tradeType;
    }

    @Override
    public int getHeadLength() {
        return 0;
    }

    @Override
    public byte[] getHeadMessage() {
        return new byte[0];
    }
}
