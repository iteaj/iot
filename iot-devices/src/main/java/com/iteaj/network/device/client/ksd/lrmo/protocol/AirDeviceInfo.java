package com.iteaj.network.device.client.ksd.lrmo.protocol;

import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.iot.client.modbus.message.ModbusStandardPdu;
import com.iteaj.network.device.consts.AirSetType;
import com.iteaj.network.device.consts.AirWindType;
import com.iteaj.network.device.consts.SwitchStatus;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.ksd.lrmo.AirCode;
import com.iteaj.network.device.client.ksd.lrmo.AirType;

/**
 * 获取当前空调的信息
 */
public class AirDeviceInfo extends AirModbusProtocol {

    // 当前室内温度
    private double temp;
    // 当前设备设定的温度
    private double setTemp;
    /**
     * 0：开机
     * 1：关机
     * 设备状态 开机或者关机
     */
    private SwitchStatus status;
    /**
     * 模式设定
     * 1：制热模式
     * 3：制冷模式
     * 9：通风模式
     */
    private AirSetType setType;
    /**
     * 风速当前处于档位
     * 1：低速
     * 2：中速
     * 3：高速
     */
    private int currentWind;
    /**
     * 风速设定
     * 1：低速
     * 2：中速
     * 3：高速
     * 4：自动
     */
    private AirWindType setWindType;

    public AirDeviceInfo(String gateway, String equipCode) {
        super(gateway, equipCode, AirCode.Read);
    }

    @Override
    protected short getStartAddr() {
        return 0x0000;
    }

    @Override
    protected short getReadNum() { // 读取六个寄存器
        return 0x0006;
    }

    @Override
    protected byte[] getData() {
        return new byte[0];
    }

    @Override
    public AirType protocolType() {
        return AirType.AirInfo;
    }

    @Override
    public void doBuildResponseMessage(ModbusStandardMessage message) {
        ModbusStandardPdu body = message.getBody();
        byte[] data = body.getData();
        this.temp = ByteUtil.bytesToShort(data, 0) * 0.1; // 当前温度
        this.setTemp = ByteUtil.bytesToShort(data, 2) * 0.1; // 设定的温度
        this.status = ByteUtil.bytesToShort(data, 4) == 0 ? SwitchStatus.on : SwitchStatus.off;
        short type = ByteUtil.bytesToShort(data, 6);
        this.setType = type == 1 ? AirSetType.制热 : type == 3 ? AirSetType.制冷 : type == 9 ? AirSetType.送风 : AirSetType.抽湿;
        this.currentWind = ByteUtil.bytesToShort(data, 8);
        short setWind = ByteUtil.bytesToShort(data, 10);
        switch (setWind) {
            case 1: this.setWindType = AirWindType.small; break;
            case 2: this.setWindType = AirWindType.middle; break;
            case 3: this.setWindType = AirWindType.gale; break;
            case 4: this.setWindType = AirWindType.auto; break;
        }
    }

    public double getTemp() {
        return temp;
    }

    public double getSetTemp() {
        return setTemp;
    }

    public SwitchStatus getStatus() {
        return status;
    }

    public AirSetType getSetType() {
        return setType;
    }

    public AirWindType getSetWindType() {
        return setWindType;
    }

    public int getCurrentWind() {
        return currentWind;
    }


}
