package com.iteaj.network.device.server.ths;

import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.consts.AirSetDire;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.utils.CommonCheckUtil;

import java.io.IOException;

/**
 * 设定空调的风向
 * @see com.iteaj.network.device.consts.AirSetDire
 */
public class ThsAirSetDire extends ThsAirProtocolAbstract {

    private AirSetDire airSetDire;

    public ThsAirSetDire(String equipCode, AirSetDire airSetDire) {
        super(equipCode);
        this.airSetDire = airSetDire;
    }

    @Override
    public ElfinType protocolType() {
        return ElfinType.Air_Set_Type;
    }

    @Override
    protected ThsElfinMessage doBuildRequestMessage() throws IOException {
        if(getAirSetDire() == null) {
            throw new ProtocolException("请指定空调风向");
        }

        byte[] message = new byte[5];
        ByteUtil.addBytes(message, ByteUtil.hexToByte("08"), 0);
        switch (getAirSetDire()) {
            case auto: ByteUtil.addBytes(message, ByteUtil.hexToByte("00"), 1); break;
            case hand: ByteUtil.addBytes(message, ByteUtil.hexToByte("01"), 1); break;
        }

        ByteUtil.addBytes(message, ByteUtil.hexToByte("08"), 2);
        ByteUtil.addBytes(message, ByteUtil.hexToByte("08"), 3);

        byte xor = CommonCheckUtil.getXor(ByteUtil.subBytes(message, 0, 4));
        message[4] = xor;

        ElfinMessageHeader messageHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
        return new ThsElfinMessage(message, messageHeader);
    }

    public AirSetDire getAirSetDire() {
        return airSetDire;
    }

    public void setAirSetDire(AirSetDire airSetDire) {
        this.airSetDire = airSetDire;
    }
}
