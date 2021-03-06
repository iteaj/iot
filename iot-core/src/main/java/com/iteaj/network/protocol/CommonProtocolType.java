package com.iteaj.network.protocol;

import com.iteaj.network.ProtocolType;

/**
 *  <p>协议类型</p>
 * 协议头里面的交易码
 *
 * Create Date By 2017-09-08
 * @author iteaj
 * @since 1.7
 */
public enum CommonProtocolType implements ProtocolType {

    // 通用的心跳协议
    Common_Heart(88888, "心跳协议"),
    TCClint(99999, "后台管理客户端协议"),
    /* 设备响应的协议没有找到对应的平台请求报文协议 */
    NoneMap(95555, "设备响应的报文找不到对应的平台报文");

    public int value; //交易码值
    public String desc; //交易描述

    CommonProtocolType(int value, String desc) {
        this.desc = desc;
        this.value = value;
    }

    @Override
    public Enum getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
