package com.iteaj.network.device.server.gps.consts;

/**
 * create time: 2021/1/23
 *
 * @author iteaj
 * @since 1.0
 */
public enum DIdentityStatus {

    in("从业资格证IC卡插入"), out("从业资格证IC卡拔出")

    ;

    public String desc;

    DIdentityStatus(String desc) {
        this.desc = desc;
    }
}
