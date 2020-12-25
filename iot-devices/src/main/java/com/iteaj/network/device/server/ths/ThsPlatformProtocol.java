package com.iteaj.network.device.server.ths;

import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import com.iteaj.network.server.service.PlatformRequestService;

public abstract class ThsPlatformProtocol extends PlatformRequestProtocol<ThsElfinMessage> {

    public ThsPlatformProtocol(String equipCode) {
        super(equipCode);
    }

    @Override
    protected String doGetMessageId() {
        return getEquipCode();
    }

    @Override
    public abstract ElfinType protocolType();
}
