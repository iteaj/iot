package com.iteaj.network.client;

public class ClientRelationEntity {

    private long timeout;
    private String equipCode;
    private String messageId;
    private String tradeType;

    public ClientRelationEntity(long timeout, String tradeType, String equipCode, String messageId) {
        this.timeout = timeout;
        this.tradeType = tradeType;
        this.equipCode = equipCode;
        this.messageId = messageId;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getEquipCode() {
        return equipCode;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
