package com.iteaj.iot.client.modbus.protocol;

import com.iteaj.iot.client.ClientRequestProtocol;
import com.iteaj.iot.client.modbus.message.ModbusStandardHeader;
import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.iot.client.modbus.message.ModbusStandardPdu;

/**
 * 标准的modbus协议
 */
public abstract class ModbusStandardProtocol extends ClientRequestProtocol<ModbusStandardMessage> {

    @Override
    public ModbusStandardProtocol buildRequestMessage() {
        ModbusStandardPdu pdu = ModbusStandardPdu.buildRequestPdu(getCode(), getStartAddr(), getReadNum(), getData());
        ModbusStandardHeader modbusStandardHeader = ModbusStandardHeader.buildRequestHeader(getUnitId(), (short) pdu.getBodyLength());

        this.requestMessage = ModbusStandardMessage.buildRequestMessage(modbusStandardHeader, pdu);

        if(logger.isDebugEnabled()) {
            logger.info("构建的modbus请求报文 - {}", this.requestMessage());
        }

        return this;
    }

    protected abstract byte getUnitId();

    protected abstract byte getCode();

    /**
     * 获取从那个寄存器地址开始
     * @return
     */
    protected abstract short getStartAddr();

    /**
     * 获取要读取的寄存器数量 只在读取的时候有用
     * @return
     */
    protected abstract short getReadNum();

    /**
     * 要设置的寄存器数据
     * @return
     */
    protected abstract byte[] getData();

    protected boolean isRead() {
        return getCode() == 3;
    }

    protected boolean isWrite() {
        return getCode() == 6;
    }
}
