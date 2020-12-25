package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.Message;
import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduTradeType;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;

public abstract class PduDeviceProtocol extends DeviceRequestProtocol<PduMessage> {

    public PduDeviceProtocol(PduMessage requestMessage) {
        super(requestMessage);
        Message.MessageHead head = requestMessage.getHead();
        if(head != null) this.setEquipCode(head.getEquipCode());
    }

    @Override
    protected PduMessage doBuildResponseMessage() {
        return null;
    }

    @Override
    public abstract PduTradeType protocolType();

    @Override
    public String desc() {
        return "智慧融合控制台协议 - " + protocolType();
    }
}
