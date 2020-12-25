package com.iteaj.network.device.server.ths;

import cn.hutool.core.date.DateUtil;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.consts.AirWindType;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.utils.CommonCheckUtil;

import java.io.IOException;
import java.util.Date;

/**
 * 设定空调的风速
 * @see com.iteaj.network.device.consts.AirWindType
 */
public class ThsAirSetWind extends ThsAirProtocolAbstract {

    private AirWindType airWindType;

    public ThsAirSetWind(String equipCode, AirWindType airWindType) {
        super(equipCode);
        this.airWindType = airWindType;
    }

    @Override
    public ElfinType protocolType() {
        return ElfinType.Air_Set_Type;
    }

    @Override
    protected ThsElfinMessage doBuildRequestMessage() throws IOException {
        if(getAirWindType() == null) {
            throw new ProtocolException("请指定空调风速模式");
        }

        byte[] message = new byte[5];
        ByteUtil.addBytes(message, ByteUtil.hexToByte("07"), 0);
        switch (getAirWindType()) {
            case auto: ByteUtil.addBytes(message, ByteUtil.hexToByte("00"), 1); break;
            case small: ByteUtil.addBytes(message, ByteUtil.hexToByte("01"), 1); break;
            case middle: ByteUtil.addBytes(message, ByteUtil.hexToByte("02"), 1); break;
            case gale: ByteUtil.addBytes(message, ByteUtil.hexToByte("03"), 1); break;
        }

        Date date = new Date();
        message[2] = (byte) DateUtil.hour(date, true);
        message[3] = (byte) DateUtil.minute(date);

        byte xor = CommonCheckUtil.getXor(ByteUtil.subBytes(message, 0, 4));
        message[4] = xor;

        ElfinMessageHeader messageHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
        return new ThsElfinMessage(message, messageHeader);
    }

    public AirWindType getAirWindType() {
        return airWindType;
    }

    public void setAirWindType(AirWindType airWindType) {
        this.airWindType = airWindType;
    }
}
