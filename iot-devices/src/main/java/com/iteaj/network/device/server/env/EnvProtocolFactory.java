package com.iteaj.network.device.server.env;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.Message;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.server.protocol.NoneDealProtocol;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;

public class EnvProtocolFactory extends ProtocolFactory<EnvElfinMessage> {

    @Override
    public AbstractProtocol getProtocol(EnvElfinMessage message) {
        Message.MessageHead head = message.getHead();

        // 心跳包不需要处理
        if(head.getTradeType() == ElfinType.Env_Elfin_Heart) {
            return NoneDealProtocol.getInstance(head.getEquipCode());
        } else {
            return ((PlatformRequestProtocol)remove(head.getMessageId())).buildResponseMessage(message);
        }
    }
}
