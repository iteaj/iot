package com.iteaj.network.device.server.pdu;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.Message;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.ProtocolTimeoutStorage;
import com.iteaj.network.device.server.pdu.protocol.*;
import com.iteaj.network.server.protocol.NoneDealProtocol;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;

public class PduServerProtocolFactory extends ProtocolFactory<PduMessage> {

    public PduServerProtocolFactory(ProtocolTimeoutStorage delegation) {
        super(delegation);
    }

    @Override
    public AbstractProtocol getProtocol(PduMessage message) {
        Message.MessageHead head = message.getHead();
        PduTradeType tradeType = head.getTradeType();

        if(tradeType == PduTradeType.心跳) {
            if(logger.isTraceEnabled()) {
                logger.trace("客户端设备心跳 - 服务组件: 智慧融合控制台设备 - 设备编号: {}", head.getEquipCode());
            }

            return NoneDealProtocol.getInstance(head.getEquipCode());
        } else if(tradeType == PduTradeType.登录)
            return new LoginProtocol(message).buildRequestMessage();
        else if(tradeType == PduTradeType.iostate)
            return new IosStateProtocol(message).buildRequestMessage();
        else if(tradeType == PduTradeType.remark)
            return new RemarkProtocol(message).buildRequestMessage();
        else if(tradeType == PduTradeType.PVC_Info)
            return new PvcInfoProtocol(message).buildRequestMessage();
        else if(tradeType == PduTradeType.status) {
            String messageId = head.getMessageId();
            if(null == messageId) return null;

            return ((PlatformRequestProtocol) remove(messageId)).buildResponseMessage(message);
        } else if(tradeType == PduTradeType.PVC_setup) {
            PlatformRequestProtocol remove = (PlatformRequestProtocol)remove(head.getMessageId());
            if(remove == null) return new PvcSetupProtocol(head.getEquipCode()).buildResponseMessage(message);
            else return remove.buildResponseMessage(message);
        } else if(tradeType == PduTradeType.warnning) {
            return new PduWarningProtocol(message).buildRequestMessage();
        } else if(tradeType == PduTradeType.limit) {
            return new PduLimitProtocol(message).buildRequestMessage();
        }

        else if(tradeType == PduTradeType.handcontrol)
            return ((PlatformRequestProtocol)remove(head.getMessageId())).buildResponseMessage(message);

        return null;
    }
}
