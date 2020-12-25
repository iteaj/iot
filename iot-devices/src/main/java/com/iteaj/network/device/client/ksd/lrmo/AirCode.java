package com.iteaj.network.device.client.ksd.lrmo;

/**
 * 空调控制器, modbus功能码
 */
public enum AirCode {

    Read((byte) 0x03), Write((byte) 0x06);

    public byte code;

    AirCode(byte code) {
        this.code = code;
    }
}
