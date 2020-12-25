package com.iteaj.network.device.server.ths;

import cn.hutool.core.date.DateUtil;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.utils.CommonCheckUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

public class ThsAirSetTemp extends ThsAirProtocolAbstract{

    /**
     * 十六进制温度值
     */
    private String hexTemp;

    public ThsAirSetTemp(String equipCode, String hexTemp) {
        super(equipCode);
        this.hexTemp = hexTemp;
    }

    @Override
    public ElfinType protocolType() {
        return ElfinType.Air_Set_Temp;
    }

    @Override
    protected ThsElfinMessage doBuildRequestMessage() throws IOException {
        if(!StringUtils.hasText(getHexTemp())) {
            throw new ProtocolException("请指定空调温度");
        }

        byte[] bytes = ByteUtil.hexToByte(this.getHexTemp());

        byte[] message = new byte[5];
        ByteUtil.addBytes(message, ByteUtil.hexToByte("06"), 0);
        ByteUtil.addBytes(message, bytes, 1);

        Date date = new Date();
        message[2] = (byte) DateUtil.hour(date, true);
        message[3] = (byte) DateUtil.minute(date);

        byte xor = CommonCheckUtil.getXor(ByteUtil.subBytes(message, 0, 4));
        message[4] = xor;

        ElfinMessageHeader messageHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
        return new ThsElfinMessage(message, messageHeader);
    }

    public String getHexTemp() {
        return hexTemp;
    }

    public void setHexTemp(String hexTemp) {
        this.hexTemp = hexTemp;
    }
}
