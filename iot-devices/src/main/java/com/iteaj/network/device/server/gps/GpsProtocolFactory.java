package com.iteaj.network.device.server.gps;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.Message;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.device.server.gps.protocol.*;

/**
 * create time: 2021/1/14
 *
 * @author iteaj
 * @since 1.0
 */
public class GpsProtocolFactory extends ProtocolFactory<GpsMessage> {

    @Override
    public AbstractProtocol getProtocol(GpsMessage message) {
        Message.MessageHead head = message.getHead();
        GpsProtocolType type = head.getTradeType();
        switch (type) {
            case Heart:
                return new HeartProtocol(message).buildRequestMessage();
            case TRegister:
                return new TRegisterProtocol(message).buildRequestMessage();
            case TAuth:
                return new TAuthProtocol(message).buildRequestMessage();
            case DIdentity:
                return new DIdentityProtocol(message).buildRequestMessage();
            case PReport:
                return new PReportProtocol(message).buildRequestMessage();
            case Unknown:
                System.out.println("未知协议：" + head);
        }
        return null;
    }
}
