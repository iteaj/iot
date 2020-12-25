package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduTradeType;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;

public abstract class PduPlatformProtocol extends PlatformRequestProtocol {
    public PduPlatformProtocol(String equipCode) {
        super(equipCode);
    }

    @Override
    public abstract PduTradeType protocolType();

    protected PduMessage newMessage(byte[] message) {
        PduMessage.PduMessageHead messageHead = new PduMessage
                .PduMessageHead(getMessageId(), getEquipCode(), protocolType());
        return new PduMessage(messageHead, message);
    }

    @Override
    protected String doGetMessageId() {
        // 获取十二位的messageId
        return String.valueOf(System.currentTimeMillis()/10);
    }
}
