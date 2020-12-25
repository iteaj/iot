package com.iteaj.network.device.server.ths;

import cn.hutool.core.date.DateUtil;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.consts.AirSetType;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.utils.CommonCheckUtil;

import java.io.IOException;
import java.util.Date;

/**
 * 设定空调的运行模式
 * @see com.iteaj.network.device.consts.AirSetType
 */
public class ThsAirSetType extends ThsAirProtocolAbstract {

    private AirSetType airSetType;

    public ThsAirSetType(String equipCode, AirSetType setType) {
        super(equipCode);
        this.airSetType = setType;
    }

    @Override
    public ElfinType protocolType() {
        return ElfinType.Air_Set_Type;
    }

    @Override
    protected ThsElfinMessage doBuildRequestMessage() throws IOException {
        if(getAirSetType() == null) {
            throw new ProtocolException("请指定空调运行模式");
        }

        byte[] message = new byte[5];
        ByteUtil.addBytes(message, ByteUtil.hexToByte("05"), 0);
        switch (getAirSetType()) {
            case 自动: ByteUtil.addBytes(message, ByteUtil.hexToByte("00"), 1); break;
            case 制冷: ByteUtil.addBytes(message, ByteUtil.hexToByte("01"), 1); break;
            case 抽湿: ByteUtil.addBytes(message, ByteUtil.hexToByte("02"), 1); break;
            case 送风: ByteUtil.addBytes(message, ByteUtil.hexToByte("03"), 1); break;
            case 制热: ByteUtil.addBytes(message, ByteUtil.hexToByte("04"), 1); break;
        }

        Date date = new Date();
        message[2] = (byte) DateUtil.hour(date, true);
        message[3] = (byte) DateUtil.minute(date);

        byte xor = CommonCheckUtil.getXor(ByteUtil.subBytes(message, 0, 4));
        message[4] = xor;

        ElfinMessageHeader messageHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
        return new ThsElfinMessage(message, messageHeader);
    }

    public AirSetType getAirSetType() {
        return airSetType;
    }

    public void setAirSetType(AirSetType airSetType) {
        this.airSetType = airSetType;
    }
}
