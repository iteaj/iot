package com.iteaj.network.client;

import com.alibaba.fastjson.JSONObject;
import com.iteaj.network.client.app.AppClientType;
import com.iteaj.network.message.UnParseBodyMessage;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class AppClientServerMessage extends UnParseBodyMessage {

    private String deviceSn;
    private JSONObject jsonMessage;
    private AppClientType clientType;
    public AppClientServerMessage(ByteBuf byteBuf, String deviceSn) {
        super(byteBuf);
        this.deviceSn = deviceSn;
    }

    @Override
    public UnParseBodyMessage build() throws IOException {
        String clientMessage = new String(getMessage(), "UTF-8");
        this.jsonMessage = JSONObject.parseObject(clientMessage);

        this.clientType = AppClientType.getInstance(this.jsonMessage.getString("clientType"));
        this.messageHead = new AppClientServerHead(deviceSn, this.jsonMessage.getString("messageId"), this.clientType);
        return this;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public AppClientType getClientType() {
        return clientType;
    }

    public JSONObject getJsonMessage() {
        return jsonMessage;
    }
}
