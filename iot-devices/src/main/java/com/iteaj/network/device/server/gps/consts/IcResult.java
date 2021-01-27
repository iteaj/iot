package com.iteaj.network.device.server.gps.consts;

/**
 * create time: 2021/1/23
 *  IC卡插拔结果
 * @author iteaj
 * @since 1.0
 */
public enum IcResult {

    success("成功"),
    unAuth("密钥认证未通过"),
    lock("卡片被锁定"),
    out("卡片被拔出"),
    failed("数据校验错误")
    ;

    public String desc;

    IcResult(String desc) {
        this.desc = desc;
    }
}
