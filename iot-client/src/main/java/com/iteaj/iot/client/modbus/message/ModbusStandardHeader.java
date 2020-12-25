package com.iteaj.iot.client.modbus.message;

import com.iteaj.network.Message;
import com.iteaj.network.utils.ByteUtil;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @see ModbusStandardPdu
 */
public class ModbusStandardHeader implements Message.MessageHead{

    private byte unitId; // 单元编号 1 byte (用于设置从机设备的设备编号)
    private short length; // unitId + Pdu长度 2 byte
    private short protocolId = 0x00; // 协议标识 2 byte, 0x00 标识modbus协议
    private byte[] messageHeader = new byte[7];
    private static AtomicInteger transactionId = new AtomicInteger(0); // 传输标识 2 byte

    private String messageId;

    protected ModbusStandardHeader() {}

    /**
     * 设备id, 和pdu长度
     * @param unitId
     * @param pduLength
     * @return
     */
    public static ModbusStandardHeader buildRequestHeader(byte unitId, short pduLength) {
        int andIncrement = transactionId.getAndIncrement();
        if(andIncrement > 0xffff - 3) {
            transactionId.getAndSet(0);
            andIncrement = transactionId.getAndIncrement();
        }

        ModbusStandardHeader modbusStandardHeader = new ModbusStandardHeader();
        modbusStandardHeader.messageId = ByteUtil.byteToHex(unitId) + ByteUtil.shortToHex((short) andIncrement);
        modbusStandardHeader.length = (short) (pduLength + 1);
        modbusStandardHeader.unitId = unitId;

        ByteUtil.addBytes(modbusStandardHeader.messageHeader, ByteUtil.shortToBytes((short) andIncrement), 0); // transactionId
        ByteUtil.addBytes(modbusStandardHeader.messageHeader, ByteUtil.shortToBytes(modbusStandardHeader.protocolId), 2); // protocolId
        ByteUtil.addBytes(modbusStandardHeader.messageHeader, ByteUtil.shortToBytes(modbusStandardHeader.length), 4); // length
        ByteUtil.addBytes(modbusStandardHeader.messageHeader, new byte[]{modbusStandardHeader.unitId}, 6); // unitId(deviceSn)

        return modbusStandardHeader;
    }

    public static ModbusStandardHeader buildResponseHeader(byte[] message) {
        short transactionId = ByteUtil.bytesToShort(message, 0);
        short length = ByteUtil.bytesToShort(message, 4);
        byte deviceId = ByteUtil.getByte(message, 6);
        ModbusStandardHeader modbusStandardHeader = new ModbusStandardHeader();
        modbusStandardHeader.unitId = deviceId;
        modbusStandardHeader.length = length;
        modbusStandardHeader.messageId = ByteUtil.byteToHex(deviceId) + ByteUtil.shortToHex(transactionId);
        modbusStandardHeader.messageHeader = message;
        return modbusStandardHeader;
    }

    @Override
    public String getEquipCode() {
        return String.valueOf(this.unitId);
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public <T> T getTradeType() {
        throw new UnsupportedOperationException("不支持的操作");
    }

    public byte getUnitId() {
        return unitId;
    }

    public void setUnitId(byte unitId) {
        this.unitId = unitId;
    }

    public int getLength() {
        return length;
    }

    public short getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(short protocolId) {
        this.protocolId = protocolId;
    }

    @Override
    public int getHeadLength() {
        return messageHeader.length;
    }

    @Override
    public byte[] getHeadMessage() {
        return messageHeader;
    }

    @Override
    public String toString() {
        return "ModbusStandardHeader{" +
                "unitId=" + unitId +
                ", length=" + length +
                ", protocolId=" + protocolId +
                ", messageId='" + messageId +
                ", messageHeader=" + Arrays.toString(messageHeader) + '\'' +
                '}';
    }
}
