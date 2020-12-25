package com.iteaj.network.device.client.ksd.lrmo.protocol;

import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.ksd.lrmo.AirCode;
import com.iteaj.network.device.client.ksd.lrmo.AirType;

/**
 * 设定空调的温度
 */
public class AirSetTemp extends AirModbusProtocol {

    private short temp; // 设定的温度

    /**
     * @param equipCode 设备编号 1-99
     */
    public AirSetTemp(String gateway, String equipCode, short temp) {
        super(gateway, equipCode, AirCode.Write);
        this.temp = temp;
    }

    @Override
    public AirType protocolType() {
        return AirType.Set_Temp;
    }

    @Override
    protected short getStartAddr() {
        return 0x0001;
    }

    @Override
    protected short getReadNum() {
        return 0;
    }

    @Override
    protected byte[] getData() {
        return ByteUtil.shortToBytes(this.temp);
    }

    @Override
    public void doBuildResponseMessage(ModbusStandardMessage message) {

    }

    public short getTemp() {
        return temp;
    }

    public void setTemp(short temp) {
        this.temp = temp;
    }
}
