package com.iteaj.iot.client.modbus.message;

import com.iteaj.network.client.ClientMessage;
import com.iteaj.network.utils.ByteUtil;

/**
 *  请求：
 *   00 00  00 00  00 06    FF      03    00 01  00 00
 *  | 事务 |协议标识| 长度 | 单元标识 |功能码|起始地址|寄存器地址|
 *
 *  响应：
 *   00 00  00 00  00 06     FF     03   00 01   00 00
 *  | 事务 |协议标识| 长度 | 单元标识 |功能码|字节个数|请求的数据|
 *
 * 标准的modbus报文协议格式
 */
public class ModbusStandardMessage extends ClientMessage {

    private ModbusStandardPdu pdu;
    private ModbusStandardHeader header;

    protected ModbusStandardMessage(String deviceSn, byte[] message) {
        super(deviceSn, message);
    }

    protected ModbusStandardMessage(byte[] message) {
        super(message);
    }

    public static ModbusStandardMessage buildRequestMessage(ModbusStandardHeader header, ModbusStandardPdu pdu) {
        // 计算总报文长度, 报文头7个字节
        byte[] message = new byte[7 + pdu.getBodyLength()];

        // 将报文头和报文实体合并成完整的报文
        ByteUtil.addBytes(message, header.getHeadMessage(), 0);
        ByteUtil.addBytes(message, pdu.getBodyMessage(), 7);

        // 创建报文对象
        ModbusStandardMessage modbusStandardMessage = new ModbusStandardMessage(header.getEquipCode(), message);

        modbusStandardMessage.header = header;
        modbusStandardMessage.pdu = pdu;
        return modbusStandardMessage;
    }

    public static ModbusStandardMessage buildResponseMessage(byte[] message) {
        ModbusStandardHeader modbusStandardHeader = ModbusStandardHeader.buildResponseHeader(ByteUtil.subBytes(message, 0, 7));
        ModbusStandardPdu modbusStandardPdu = ModbusStandardPdu.buildResponsePdu(ByteUtil.subBytes(message, 7));
        ModbusStandardMessage modbusStandardMessage = new ModbusStandardMessage(modbusStandardHeader.getEquipCode(), message);

        modbusStandardMessage.pdu = modbusStandardPdu;
        modbusStandardMessage.header = modbusStandardHeader;
        return modbusStandardMessage;
    }

    @Override
    public String getMessageId() {
        return getHead().getMessageId();
    }

    @Override
    public ModbusStandardHeader getHead() {
        return this.header;
    }

    @Override
    public ModbusStandardPdu getBody() {
        return this.pdu;
    }

    @Override
    public String toString() {
        return "ModbusMessage{" +
                "header=" + header +
                ", pdu=" + pdu +
                '}';
    }
}
