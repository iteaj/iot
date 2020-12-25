package com.iteaj.network.device.client.ksd.lrmo.protocol;

import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.client.ksd.lrmo.AirCode;
import com.iteaj.network.device.client.ksd.lrmo.AirType;
import com.iteaj.network.device.consts.AirSetType;
import com.iteaj.network.utils.ByteUtil;

/**
 * 写空调的运行模式
 */
public class AirSetModel extends AirModbusProtocol {

    /**
     * 设定的模式
     */
    private AirSetType setType;

    /**
     * @param equipCode 设备编号 1-99
     */
    public AirSetModel(String gateway, String equipCode, AirSetType type) {
        super(gateway, equipCode, AirCode.Write);
        this.setType = type;
    }

    @Override
    public AirType protocolType() {
        return AirType.Set_Model;
    }

    @Override
    protected short getStartAddr() {
        return 0x0003;
    }

    @Override
    protected short getReadNum() {
        return 0;
    }

    @Override
    protected byte[] getData() {
        if(getSetType() == AirSetType.制热) {
            return ByteUtil.shortToBytes((short) 1);
        } else if(getSetType() == AirSetType.送风) {
            return ByteUtil.shortToBytes((short) 9);
        } else if(getSetType() == AirSetType.制冷) {
            return ByteUtil.shortToBytes((short) 3);
        } else {
            throw new ProtocolException("不支持的运行模式: " + getSetType());
        }
    }

    public AirSetType getSetType() {
        return setType;
    }

    public void setSetType(AirSetType setType) {
        this.setType = setType;
    }

    @Override
    public void doBuildResponseMessage(ModbusStandardMessage message) {

    }
}
