package com.iteaj.network.device.server.env.m702;

import com.iteaj.network.device.elfin.ElfinGatewayDecode;
import com.iteaj.network.device.elfin.ElfinMessage;
import com.iteaj.network.device.elfin.ElfinType;

/**
 * 环境7合1设备解码器
 */
public class EnvM702Decoder extends ElfinGatewayDecode {

    public EnvM702Decoder() {
        super();
    }

    @Override
    protected ElfinMessage buildMessage(String deviceSn, byte[] message) {
        return new EnvM702Message(message, deviceSn, ElfinType.Env_Elfin_M702).build();
    }

    @Override
    protected ElfinMessage getHeartMessage(String deviceSn, byte[] message) {
        return new EnvM702Message(message, deviceSn, ElfinType.Env_Elfin_Heart).build();
    }
}
