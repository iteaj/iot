package com.iteaj.network.device.client.ksd.lrmo.protocol;

import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.consts.SwitchStatus;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.ksd.lrmo.AirCode;
import com.iteaj.network.device.client.ksd.lrmo.AirType;

/**
 * 设置或者读取控制面板的开关状态
 */
public class AirStatusController extends AirModbusProtocol {

    /**
     * 2byte
     * 0：开机
     * 1：关机
     */
    private SwitchStatus status;

    /**
     * @param equipCode 设备编号 1-99
     */
    public AirStatusController(String gateway, String equipCode) {
        super(gateway, equipCode, AirCode.Read);
    }

    /**
     *
     * @param equipCode
     * @param status
     */
    public AirStatusController(String gateway, String equipCode, SwitchStatus status) {
        super(gateway, equipCode, AirCode.Write);
        this.status = status;
    }

    @Override
    public AirType protocolType() {
        return AirType.Status_Controller;
    }

    @Override
    protected short getStartAddr() {
        return protocolType().ex;
    }

    @Override
    protected short getReadNum() {
        return 0x0002;
    }

    @Override
    protected byte[] getData() {
        if(status == null)
            throw new ProtocolException("未指定状态");
        else {
            if(status == SwitchStatus.on) {
                return ByteUtil.shortToBytes((short) 0x0000);
            } else {
                return ByteUtil.shortToBytes((short) 0x0001);
            }
        }
    }

    @Override
    public void doBuildResponseMessage(ModbusStandardMessage message) {
        byte[] data = message.getBody().getData();
        short status = ByteUtil.bytesToShort(data, 0);
        if(status == 0) this.status = SwitchStatus.on;
        else this.status = SwitchStatus.off;

    }

    public SwitchStatus getStatus() {
        return status;
    }
}
