package com.iteaj.network.client;

import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.utils.MessageUtil;

import java.io.IOException;
import java.util.UUID;

public class ClientMessage extends UnParseBodyMessage {

    // 要操作的设备的设备编号
    private String deviceSn;
    private String messageId;

    public ClientMessage(byte[] message) {
        this( null, message);
    }

    @Override
    public UnParseBodyMessage build() throws IOException {
        throw new UnsupportedOperationException("客户端报文不支持构建");
    }

    public ClientMessage(String deviceSn, byte[] message) {
        super(message);
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getMessageId() {
        if(messageId != null) return messageId;

        synchronized (ClientMessage.class) {
            if(messageId != null) return messageId;

            return this.messageId = UUID.randomUUID().toString();
        }
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public String toString() {
        return "ClientMessage{" +
                "deviceSn='" + deviceSn + '\'' +
                ", messageId='" + messageId + '\'' +
                ", messageHead=" + messageHead +
                ", message=" + ByteUtil.bytesToHex(getMessage() != null ? getMessage() : emptyMessage) +
                '}';
    }
}
