package com.iteaj.iot.client.json;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.ProtocolTimeoutStorage;
import com.iteaj.network.client.app.AppClientMessage;

public class AppClientFactory extends ProtocolFactory<AppClientMessage> {

    public AppClientFactory(ProtocolTimeoutStorage delegation) {
        super(delegation);
    }

    @Override
    public AbstractProtocol getProtocol(AppClientMessage message) {
        AbstractProtocol remove = remove(message.getMessageId());
        return remove;
    }
}
