package com.iteaj.network.client.app;

import com.iteaj.network.Message;

import java.beans.Transient;

public class AppClientMessageHead implements Message.MessageHead {

    /**
     * 超时时间
     */
    private long timeout;
    /**
     * 是否等待设备端返回
     */
    private boolean waiting;
    /**
     * 应用程序客户端的设备编号, 可为null
     * @see AppClientMessage#getDeviceSn() 要操作的设备的设备编号 必填
     */
    private String equipCode;
    private String messageId;
    private Object tradeType;

    protected AppClientMessageHead() { }

    public AppClientMessageHead(String equipCode, String messageId, Object tradeType, long timeout) {
        this.timeout = timeout;
        this.equipCode = equipCode;
        this.messageId = messageId;
        this.tradeType = tradeType;
    }

    public AppClientMessageHead(String equipCode, String messageId, Object tradeType) {
        this(equipCode, messageId, tradeType, 0);
    }

    @Override
    public String getEquipCode() {
        return this.equipCode;
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public <T> T getTradeType() {
        return (T) this.tradeType;
    }

    @Override
    public int getHeadLength() {
        return 0;
    }

    @Override
    @Transient
    public byte[] getHeadMessage() {
        return new byte[0];
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setTradeType(Object tradeType) {
        this.tradeType = tradeType;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public AppClientMessageHead setWaiting(boolean waiting) {
        this.waiting = waiting;
        return this;
    }

}
