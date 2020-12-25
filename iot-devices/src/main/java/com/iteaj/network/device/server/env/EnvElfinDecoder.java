package com.iteaj.network.device.server.env;

import com.iteaj.network.device.elfin.ElfinGatewayDecode;
import com.iteaj.network.device.elfin.ElfinMessage;
import com.iteaj.network.device.elfin.ElfinType;

public class EnvElfinDecoder extends ElfinGatewayDecode {

    public EnvElfinDecoder() {
        super(0);
    }

    @Override
    protected ElfinMessage buildMessage(String deviceSn, byte[] message) {
        return new EnvElfinMessage(message, deviceSn, ElfinType.Env_Elfin_Info).build();
    }

    @Override
    protected ElfinMessage getHeartMessage(String deviceSn, byte[] message) {
        return new EnvElfinMessage(message, deviceSn, ElfinType.Env_Elfin_Heart).build();
    }
}
