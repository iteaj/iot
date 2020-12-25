package com.iteaj.network.device.server.pdu;

public enum PduTradeType {
    心跳, 登录, iostate, close, open, status, PVC_Info, PVC_get
    , remark,programme1,programme2, PVC_setup,PVC
    , handcontrol, warnning, limit;

    public static PduTradeType getInstance(String type) {
        switch (type) {
            case "S": return 心跳;
            case "PVC": return PVC;
            case "login": return 登录;
            case "open": return open;
            case "close": return close;

            case "limit": return limit;
            case "status": return status;
            case "iostate": return iostate;
            case "PVC_get": return PVC_get;
            case "PVC_Info": return PVC_Info;
            case "remark": return remark;
            case "warnning": return warnning;
            case "handcontrol": return handcontrol;
            case "programme1": return programme1;
            case "programme2": return programme2;
            case "PVC_setup": return PVC_setup;
            default: throw new RuntimeException("未知的交易类型： " + type);
        }
    }
}
