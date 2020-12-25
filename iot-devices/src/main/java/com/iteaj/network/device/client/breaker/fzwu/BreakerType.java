package com.iteaj.network.device.client.breaker.fzwu;

import com.iteaj.network.ProtocolType;

public enum BreakerType implements ProtocolType {

    BH_20("20"), // 心跳
    BH_31("31"),
    BH_32("32"), // 上报网关设备信息
    BH_33("33"), // 上报断路器设备信息
    BH_35("35"), // 上报电量数据 每半小时上报一次 (包含 电压，电流，功率，电量，漏电流)
    BH_36("36"), // 错误异常状态信息上报(不重合:短路 漏电 尝试重合:过压 欠压 过载)
    BH_37("37"),
    BH_39("39"), // 初始化网关
    BH_40("40"), // 下发漏电自检
    BH_41("41"), // 服务获取单相实时数据
    BH_48("48"), // 上报三相重合闸数据
    BH_49("49"), // 三相重合闸错误码--49H
    BH_4A("4A"), // 服务获取三相重合闸实时数据
    BH_4E("4E"), // 下发开关命令 (37)

    Offline("00"), // 断路器网关下线, 此类型不在设备文档里面
    ;

    public String value;

    BreakerType(String value) {
        this.value = value;
    }

    @Override
    public BreakerType getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return value;
    }
}
