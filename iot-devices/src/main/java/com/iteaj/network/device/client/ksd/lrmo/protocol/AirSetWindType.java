package com.iteaj.network.device.client.ksd.lrmo.protocol;

import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.consts.AirWindType;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.ksd.lrmo.AirCode;
import com.iteaj.network.device.client.ksd.lrmo.AirType;

public class AirSetWindType extends AirModbusProtocol {

    private AirWindType windType;


    /**
     * @param equipCode 设备编号
     */
    public AirSetWindType(String gateway, String equipCode, AirWindType windType) {
        super(gateway, equipCode, AirCode.Write);
        this.windType = windType;
    }

    @Override
    public AirType protocolType() {
        return AirType.Set_Wind_Type;
    }

    @Override
    protected short getStartAddr() {
        return 0x0005;
    }

    @Override
    protected short getReadNum() {
        return 0;
    }

    @Override
    protected byte[] getData() {
        switch (this.windType) {
            case small: return ByteUtil.shortToBytes((short) 1);
            case middle: return ByteUtil.shortToBytes((short) 2);
            case gale: return ByteUtil.shortToBytes((short) 3);
            case auto: return ByteUtil.shortToBytes((short) 4);
            default: throw new ProtocolException("错误的风速类型");
        }
    }

    @Override
    public void doBuildResponseMessage(ModbusStandardMessage message) {

    }

    public AirWindType getWindType() {
        return windType;
    }
}
