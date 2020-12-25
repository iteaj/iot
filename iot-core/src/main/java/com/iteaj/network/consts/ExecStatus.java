package com.iteaj.network.consts;

/**
 * 协议的执行状态
 */
public enum ExecStatus {
    成功("成功"), 超时("超时"), 断线("设备断线"), 失败("未知状态");

    public String desc;

    ExecStatus(String desc) {
        this.desc = desc;
    }

    public static ExecStatus getInstance(String val) {
        switch (val) {
            case "成功": return 成功;
            case "超时": return 超时;
            case "断线": return 断线;
            case "失败": return 失败;
            default: throw new IllegalArgumentException("错误的执行状态: " + val);
        }
    }
}
