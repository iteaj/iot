package com.iteaj.network.message;

import com.iteaj.network.Message;

/**
 * <p>默认的报文头</p>
 *  此报文头初始化为默认的数据值
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public class DeviceMessageHead implements Message.MessageHead {

    private byte[] message; //报文头数据

    private String messageId; //消息id
    private String equipCode; //设备号
    private Object tradeType; //协议类型

    public DeviceMessageHead(byte[] message, String messageId, String equipCode, Object tradeType) {
        this.message = message;
        this.messageId = messageId;
        this.equipCode = equipCode;
        this.tradeType = tradeType;
    }

    @Override
    public <T> T getTradeType() {
        return (T) this.tradeType;
    }

    /**
     * 消息认证id
     * @return
     */
    public String getMessageId() {
        return messageId;
    }

    public DeviceMessageHead setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    /**
     * 设备号
     * @return
     */
    public String getEquipCode() {
        return equipCode;
    }

    public DeviceMessageHead setEquipCode(String equipCode) {
        this.equipCode = equipCode;
        return this;
    }

    /**
     * 校验码 以上所有BYTE的校验和
     * @return
     */


    @Override
    public int getHeadLength() {
        return message.length;
    }

    @Override
    public byte[] getHeadMessage() {
        return message;
    }
}
