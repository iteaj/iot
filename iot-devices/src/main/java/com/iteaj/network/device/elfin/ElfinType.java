package com.iteaj.network.device.elfin;

import com.iteaj.network.ProtocolType;

public enum ElfinType implements ProtocolType {
    Env_Elfin_Heart("环境监测 - 网关心跳"),
    Env_Elfin_Info("环境监测 - 监测信息"),
    Env_Elfin_THS("环境监测 - 温湿度烟雾"),
    Env_Elfin_M702("环境监测 - 7合一环境监测"),

    Air_Unknow("空调红外设备 -  未知协议"),
    Air_Switch_status("空调红外设备 - 开关控制(需先设定空调型号)"),
    Air_Set_Model("空调红外设备 - 设定空调型号"),
    Air_Set_Temp("空调红外设备 - 设定空调温度"),
    Air_Set_Wind("空调红外设备 - 设定空调风速"),
    Air_Set_Type("空调红外设备 - 设定空调运行模式"),
        ;
    public String desc;

    ElfinType(String desc) {
        this.desc = desc;
    }

    @Override
    public ElfinType getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
