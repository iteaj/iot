package com.iteaj.network.device.server.gps;

import com.iteaj.network.ProtocolType;

/**
 * create time: 2021/1/19
 *
 * @author iteaj
 * @since 1.0
 */
public enum GpsProtocolType implements ProtocolType {

    Heart("心跳协议", "0002"),
    TResp("终端通用应答", "0001"),
    PResp("平台通用应答", "8001"),
    TLogout("终端注销", "0003"),
    PReport("位置信息汇报", "0200"),
    QPInfo("位置信息查询", "8201"),
    QPRInfo("位置信息查询应答", "0201"),
    TAuth("终端鉴权", "0102"),
    RegResp("终端注册应答", "8100"),
    TRegister("终端注册", "0100"),
    TCtrl("终端控制", "8105"),
    STParam("设置终端参数", "8103"),
    QTParam("查询终端参数", "8104"),
    QTRParam("查询终端参数应答", "0104"),
    DIdentity("驾驶员身份上报", "0702"),
    Unknown("未知协议", "");
    ;
        ;
    public String code;
    private String desc;
    GpsProtocolType(String desc, String code) {
        this.desc = desc;
        this.code = code;
    }

    @Override
    public Enum getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
