package com.iteaj.network.client;

public enum ClientTradeType {

    c_heart, c_pdu_status_switch, c_pdu_setup, c_hand_control;

    public static ClientTradeType getInstance(String type) {
        switch (type) {
            case "c_heart": return c_heart;
            case "c_pdu_setup": return c_pdu_setup;
            case "c_hand_control": return c_hand_control;
            case "c_pdu_status_switch": return c_pdu_status_switch;
            default: throw new RuntimeException("客户端协议不存在： " + type);
        }
    }
}
