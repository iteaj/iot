package com.iteaj.network.device.client.gateway.jlzn;

import com.iteaj.network.ProtocolType;

/**
 * 渐朗智能网关协议类型
 */
public enum JlznProtocolType implements ProtocolType {
    get_devlist("获取设备列表"),
    login("用户登录"),
    update_pwd("用户修改密码"),
    switch_status("开关状态切换, on是开, off是关"),
    toggle("设备置反"),
    lock("锁门"),
    unlock("开门"),
    reboot("重启设备"),
    del_dev("删除设备"),
    dev_rename("修改设备名称"),
    ;

    private String desc;

    JlznProtocolType(String desc) {
        this.desc = desc;
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
