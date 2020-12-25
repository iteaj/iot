package com.iteaj.network.client;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.AbstractProtocol;

public abstract class AbstractClientProtocol<M extends AbstractMessage> extends AbstractProtocol<M> {

    public AbstractClientProtocol(String equipCode) {
        super(equipCode);
    }

    public AbstractClientProtocol(M requestMessage, M responseMessage) {
        super(requestMessage, responseMessage);
    }
}
