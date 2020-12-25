package com.iteaj.network.server.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.protocol.ProtocolType;
import com.iteaj.network.server.service.NoneRequestService;

import java.io.IOException;

/**
 * 此协议将不做任何处理
 */
public class NoneDealProtocol extends PlatformRequestProtocol<AbstractMessage> {

    private static NoneDealProtocol instance;
    protected NoneDealProtocol(String equipCode) {
        super(equipCode);
    }

    public static NoneDealProtocol getInstance(String equipCode) {
        if(instance != null) return instance;

        instance = new NoneDealProtocol(equipCode);

        return instance;
    }

    @Override
    protected String doGetMessageId() {
        return null;
    }

    @Override
    protected AbstractMessage doBuildRequestMessage() throws IOException {
        return null;
    }

    @Override
    protected AbstractMessage resolverResponseMessage(AbstractMessage message) {
        return null;
    }

    @Override
    public ProtocolType protocolType() {
        return ProtocolType.NoneMap;
    }
}
