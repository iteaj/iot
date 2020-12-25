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

    private byte[] messageHead; //报文头数据

    private String checkSum; //校验码
    private String messageId; //消息id
    private String equipCode; //设备号
    private Object tradeType; //协议类型
//    private int totalDataLen; //数据总长度

    public DeviceMessageHead(byte[] messageHead, String messageId, String equipCode, Object tradeType) {
        this(messageHead, null, messageId, equipCode, tradeType);
    }

    public DeviceMessageHead(byte[] messageHead, String equipCode, Object tradeType) {
        this(messageHead, null, equipCode, tradeType);
    }

    public DeviceMessageHead(byte[] messageHead, String checkSum, String messageId, String equipCode, Object tradeType) {
        this.messageHead = messageHead;
        this.checkSum = checkSum;
        this.messageId = messageId;
        this.equipCode = equipCode;
        this.tradeType = tradeType;
    }

    public byte[] getMessageHead() {
        return messageHead;
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

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    /**
     * 校验码 以上所有BYTE的校验和
     * @return
     */


    @Override
    public int getHeadLength() {
        return messageHead.length;
    }

    @Override
    public byte[] getHeadMessage() {
        return messageHead;
    }
}
