package com.iteaj.iot.client;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.client.AbstractClientProtocol;

public abstract class ServerRequestClientProtocol extends AbstractClientProtocol {

    public ServerRequestClientProtocol(String equipCode) {
        super(equipCode);
    }

    public ServerRequestClientProtocol(AbstractMessage requestMessage, AbstractMessage responseMessage) {
        super(requestMessage, responseMessage);
        if(requestMessage.getHead() != null) {
            setEquipCode(requestMessage.getHead().getEquipCode());
        }
    }


    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        return this.exec(factory.getProtocolHandle(getClass()));
    }

    @Override
    public String desc() {
        return "服务端主动请求客户端协议";
    }
}
