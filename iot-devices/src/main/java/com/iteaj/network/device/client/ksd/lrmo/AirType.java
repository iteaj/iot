package com.iteaj.network.device.client.ksd.lrmo;

/**
 * 空调协议类型
 */
public enum AirType {

    AirInfo((short) 0x0000), // 获取设备信息
    Status_Controller((short) 0x0002), // 开关控制
    Set_Temp((short) 0x0001), // 设定温度
    Set_Model((short) 0x0003), // 设置模型
    Set_Wind_Type((short) 0x0005), // 设置风速类型
    Lock_Status((short) 34); // 锁状态控制

    public short ex; // 寄存器地址

    AirType(short ex) {
        this.ex = ex;
    }
}
