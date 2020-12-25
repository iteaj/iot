package com.iteaj.network.server;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.AbstractProtocol;

public abstract class AbstractServerProtocol<M extends AbstractMessage> extends AbstractProtocol<M> {

    public AbstractServerProtocol(String equipCode) {
        super(equipCode);
    }

    public AbstractServerProtocol(M requestMessage, M responseMessage) {
        super(requestMessage, responseMessage);
    }
}
