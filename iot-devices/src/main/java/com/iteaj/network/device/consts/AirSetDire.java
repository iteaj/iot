package com.iteaj.network.device.consts;

/**
 * 空调风向
 */
public enum AirSetDire {
    auto, // 自动
    hand // 手动
    ;

    public static AirSetDire getInstance(String setDire) {
        switch (setDire) {
            case "auto": return auto;
            case "hand": return hand;
            default: throw new IllegalStateException("错误的空调风向(只支持 auto, hand): " + setDire);
        }
    }
}
