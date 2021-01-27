package com.iteaj.network.device.server.gps;

/**
 * create time: 2021/1/22
 *  平台通用响应报文的状态类型
 *  0：成功/确认；1：失败；2：消息有误；3：不支持
 * @author iteaj
 * @since 1.0
 */
public enum GpsCommonRespResult {
    成功(new byte[]{0x00}),
    失败(new byte[]{0x01}),
    消息有误(new byte[]{0x02}),
    不支持(new byte[]{0x03});

    public byte[] code;

    GpsCommonRespResult(byte[] code) {
        this.code = code;
    }
}
