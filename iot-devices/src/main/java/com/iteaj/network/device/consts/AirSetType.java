package com.iteaj.network.device.consts;

public enum AirSetType {
    制冷,
    制热,
    抽湿,
    送风,
    自动
    ;


    public static AirSetType getInstance(String setType) {
        switch (setType) {
            case "制冷": return 制冷;
            case "制热": return 制热;
            case "抽湿": return 抽湿;
            case "送风": return 送风;
            case "自动": return 自动;
            default: throw new IllegalStateException("错误的空调运行类型: " + setType);
        }
    }
}
