package com.iteaj.iot.client.modbus.message;

import com.iteaj.network.Message;
import com.iteaj.network.utils.ByteUtil;

public class ModbusStandardPdu implements Message.MessageBody {

    /**
     * 请求报文参数
     */
    private byte code; // 功能码
    private short start; // 开始地址
    private short ex; // 寄存器地址

    /**
     * 响应报文参数
     */
    private byte dataLength; // 数据长度 1byte

    private byte[] data; // 数据
    private byte[] messageBody;
    protected ModbusStandardPdu() {}

    /**
     * 构建请求报文
     * @param code 功能吗
     * @param start 开始地址
     * @param ex 寄存器地址
     * @return
     */
    public static ModbusStandardPdu buildRequestPdu(byte code, short start, short ex, byte[] data) {
        if(data == null) data = new byte[0];

        ModbusStandardPdu modbusStandardPdu = new ModbusStandardPdu();
        modbusStandardPdu.setEx(ex);
        modbusStandardPdu.data = data;
        modbusStandardPdu.setCode(code);
        modbusStandardPdu.setStart(start);
        if(modbusStandardPdu.isRead()) {
            // 报文体由五个字节的固定长度 + 数据域的长度
            modbusStandardPdu.setMessageBody(new byte[5 + data.length]);
            ByteUtil.addBytes(modbusStandardPdu.getBodyMessage(), new byte[]{code}, 0);
            ByteUtil.addBytes(modbusStandardPdu.getBodyMessage(), ByteUtil.shortToBytes(start), 1);
            ByteUtil.addBytes(modbusStandardPdu.getBodyMessage(), ByteUtil.shortToBytes(ex), 3);
            ByteUtil.addBytes(modbusStandardPdu.getBodyMessage(), data, 5);
        } else {
            modbusStandardPdu.setMessageBody(new byte[3 + data.length]);
            ByteUtil.addBytes(modbusStandardPdu.getBodyMessage(), new byte[]{code}, 0);
            ByteUtil.addBytes(modbusStandardPdu.getBodyMessage(), ByteUtil.shortToBytes(start), 1);
            ByteUtil.addBytes(modbusStandardPdu.getBodyMessage(), data, 3);
        }

        return modbusStandardPdu;
    }

    public static ModbusStandardPdu buildResponsePdu(byte[] message) {
        ModbusStandardPdu modbusStandardPdu = new ModbusStandardPdu();
        modbusStandardPdu.messageBody = message;
        modbusStandardPdu.code = ByteUtil.getByte(message, 0);
        modbusStandardPdu.dataLength = ByteUtil.getByte(message, 1);
        modbusStandardPdu.data = ByteUtil.subBytes(message, 2);
        return modbusStandardPdu;
    }

    protected boolean isRead() {
        return this.code == 3;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public short getStart() {
        return start;
    }

    public void setStart(short start) {
        this.start = start;
    }

    public short getEx() {
        return ex;
    }

    public void setEx(short ex) {
        this.ex = ex;
    }

    @Override
    public int getBodyLength() {
        return this.messageBody.length;
    }

    @Override
    public byte[] getBodyMessage() {
        return messageBody;
    }

    public byte[] getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ByteUtil.bytesToHex(this.messageBody);
    }
}
