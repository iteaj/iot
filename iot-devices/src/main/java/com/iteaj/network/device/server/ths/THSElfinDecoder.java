package com.iteaj.network.device.server.ths;

import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.elfin.ElfinGatewayDecode;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.utils.ByteUtil;

/**
 * 空调, 温湿度, 烟雾传感器解码器
 */
public class THSElfinDecoder extends ElfinGatewayDecode {

    public THSElfinDecoder() {
        super(156);
    }

    @Override
    protected ThsElfinMessage buildMessage(String deviceSn, byte[] message) {
        // 空调协议的返回值只有一个字节
        if(message.length == 1) {
            return new ThsElfinMessage(message, deviceSn, ElfinType.Air_Unknow).build();
        }

        // 温湿度, 烟雾等协议的返回
        return new ThsElfinMessage(message, deviceSn, ElfinType.Env_Elfin_THS).build();
    }

    @Override
    protected ThsElfinMessage getHeartMessage(String deviceSn, byte[] message) {
        return new ThsElfinMessage(message, deviceSn, ElfinType.Env_Elfin_Heart).build();
    }

}
