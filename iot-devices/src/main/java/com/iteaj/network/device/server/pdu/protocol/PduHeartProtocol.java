package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduTradeType;

public class PduHeartProtocol extends PduDeviceProtocol{
    public PduHeartProtocol(PduMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.心跳;
    }

    @Override
    protected void resolverRequestMessage(PduMessage requestMessage) {
//        new PvcGetProtocol(getEquipCode()).request();
//        new PduStatusProtocol(getEquipCode(), "255", PduTradeType.close).request((PduPlatformService) null);
//        new PduHandControlProtocol(getEquipCode(), "DelayReboot", "4", 5).request();

//        PvcSetupProtocol.buildTemp(getEquipCode(), "0", "390", "50", "145").request((PduPlatformService) null);
    }
}
