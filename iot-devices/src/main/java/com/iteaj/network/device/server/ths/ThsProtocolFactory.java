package com.iteaj.network.device.server.ths;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.Message;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.ProtocolTimeoutStorage;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.server.protocol.NoneDealProtocol;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;

public class ThsProtocolFactory extends ProtocolFactory<ThsElfinMessage> {
    public ThsProtocolFactory(ProtocolTimeoutStorage delegation) {
        super(delegation);
    }

    @Override
    public AbstractProtocol getProtocol(ThsElfinMessage message) {
        Message.MessageHead head = message.getHead();

        if(head.getTradeType() == ElfinType.Env_Elfin_Heart) {
            if(logger.isTraceEnabled()) {
                logger.trace("客户端设备心跳 - 服务组件: 空调温湿度(Elfin网关) - 设备编号: {}", head.getEquipCode());
            }

            return NoneDealProtocol.getInstance(head.getEquipCode());
        } else {
            PlatformRequestProtocol requestProtocol = (PlatformRequestProtocol) remove(head.getMessageId());
            if(requestProtocol != null) {
                return requestProtocol.buildResponseMessage(message);
            } else {
                return null;
            }
        }
    }
}
