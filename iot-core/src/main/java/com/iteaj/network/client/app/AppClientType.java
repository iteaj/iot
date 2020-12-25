package com.iteaj.network.client.app;

import com.iteaj.network.ProtocolException;
import com.iteaj.network.ProtocolType;

public enum AppClientType implements ProtocolType {
    App_Client_Heart("应用程序客户端心跳协议"),
    App_Client_Server("应用程序客户端业务协议");

    private String desc;

    AppClientType(String desc) {
        this.desc = desc;
    }

    @Override
    public AppClientType getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    public static AppClientType getInstance(String type) {
        switch (type) {
            case "App_Client_Heart": return App_Client_Heart;
            case "App_Client_Server": return App_Client_Server;
            default: throw new ProtocolException("找不到客户端协议类型");
        }
    }
}
