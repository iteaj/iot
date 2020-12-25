package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduTradeType;

public class RemarkProtocol extends PduDeviceProtocol{

    public RemarkProtocol(PduMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected PduMessage doBuildResponseMessage() {
        return null;
    }

    @Override
    protected void resolverRequestMessage(PduMessage requestMessage) {

    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.remark;
    }
}
