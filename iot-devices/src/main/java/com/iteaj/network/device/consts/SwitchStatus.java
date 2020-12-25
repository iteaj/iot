package com.iteaj.network.device.consts;

/**
 * 开关状态
 */
public enum SwitchStatus {
    on, // 开
    off // 关
    ;

    public static SwitchStatus getInstance(String switchStatus) {
        switch (switchStatus) {
            case "on": return on;
            case "off": return off;
            default: throw new IllegalStateException("未知的开关状态: " + switchStatus);
        }
    }
}
