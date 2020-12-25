package com.iteaj.network.device.client.ksd.lrmo.protocol;

import com.iteaj.iot.client.modbus.message.ModbusStandardMessage;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.client.ksd.lrmo.AirCode;
import com.iteaj.network.device.client.ksd.lrmo.AirType;
import com.iteaj.network.device.consts.LockStatus;
import com.iteaj.network.utils.ByteUtil;

/**
 * 设定或者读取控制面板的锁状态
 */
public class AirLockStatusController extends AirModbusProtocol {

    /**
     * 0：正常使用
     * 1：按键锁定
     * 设置为按键锁定后，面板按键仅能
     * 进行开关机操作，其余按键无效
     * @see LockStatus#locked
     * @see LockStatus#unlocked
     */
    private LockStatus status;

    /**
     * 读锁状态
     * @param equipCode 设备编号
     */
    public AirLockStatusController(String gateway, String equipCode) {
        super(gateway, equipCode, AirCode.Read);
    }

    /**
     * 写锁状态
     * @param equipCode 设备编号 1-99
     * @param status 状态
     */
    public AirLockStatusController(String gateway, String equipCode, LockStatus status) {
        super(gateway, equipCode, AirCode.Write);
        this.status = status;
    }

    @Override
    public AirType protocolType() {
        return AirType.Lock_Status;
    }

    @Override
    protected short getStartAddr() {
        return 34; // 从第34个寄存器开始
    }

    @Override
    protected short getReadNum() {
        return 0;
    }

    @Override
    protected byte[] getData() {
        if(isWrite()) {
            if(this.status == LockStatus.unlocked) { // 未锁定
                return ByteUtil.shortToBytes((short) 0);
            } else if(this.status == LockStatus.locked) { // 锁定
                return ByteUtil.shortToBytes((short) 1);
            } else {
                throw new ProtocolException("请传入正确的锁定状态" + this.status);
            }
        }

        return new byte[0];
    }

    @Override
    public void doBuildResponseMessage(ModbusStandardMessage message) {
        byte[] data = message.getBody().getData();
        this.status = ByteUtil.bytesToShort(data, 0) == 0 ? LockStatus.unlocked : LockStatus.locked;
    }

    public LockStatus getStatus() {
        return status;
    }

    public void setStatus(LockStatus status) {
        this.status = status;
    }
}
