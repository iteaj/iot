package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduMessageUtil;
import com.iteaj.network.device.server.pdu.PduTradeType;

import java.io.IOException;

public class PvcGetProtocol extends PduPlatformProtocol {

    protected static final String s = "START PVC_get tag='%s' check='%s' END\n";

    public PvcGetProtocol(String equipCode) {
        super(equipCode);
    }

    @Override
    public boolean isRelation() {
        return false;
    }

    @Override
    protected AbstractMessage doBuildRequestMessage() throws IOException {
        String format = String.format(s, getMessageId(), "");
        int code = PduMessageUtil.getCode(format);
        format = String.format(s, getMessageId(), code);

        return new PduMessage(format.getBytes("GBK"));
    }

    @Override
    protected AbstractMessage resolverResponseMessage(AbstractMessage message) {
        return null;
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.PVC_get;
    }
}
