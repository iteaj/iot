package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.ProtocolException;

public enum BreakerStatus {

    Open("02"),
    All_Open("04"),
    Close("01"),
    ALL_Close("03");

    public String hex;

    BreakerStatus(String hex) {
        this.hex = hex;
    }

    public static BreakerStatus getInstance(String value) {
        switch (value) {
            case "01": return Close;
            case "02": return Open;
            case "03": return ALL_Close;
            case "04": return All_Open;
            default:
                throw new ProtocolException("错误的类型: " + value);
        }
    }

    public static BreakerStatus getInstanceByName(String name) {
        switch (name) {
            case "Open": return Open;
            case "All_Open": return ALL_Close;
            case "Close": return Close;
            case "ALL_Close": return All_Open;
            default:
                throw new ProtocolException("错误的类型: " + name);
        }
    }
}
