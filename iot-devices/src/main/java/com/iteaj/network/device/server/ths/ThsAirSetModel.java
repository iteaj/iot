package com.iteaj.network.device.server.ths;

import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.utils.CommonCheckUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * 设定空调型号
 * 注意: 在调用其他协议之前必须先调用此协议设定空调型号
 */
public class ThsAirSetModel extends ThsAirProtocolAbstract {

    /**
     * 空调码
     */
    private String code;

    public ThsAirSetModel(String equipCode, String code) {
        super(equipCode);
        this.code = code;
    }

    @Override
    protected ThsElfinMessage doBuildRequestMessage() throws IOException {
        if(!StringUtils.hasText(code)) {
            throw new ProtocolException("请指定空调码");
        }

        byte[] bytes = ByteUtil.shortToBytes(Short.valueOf(this.getCode()));
        if(bytes.length == 1) {
            bytes = new byte[] {0, bytes[0]};
        }

        byte[] message = new byte[5];
        ByteUtil.addBytes(message, ByteUtil.hexToByte("02"), 0);
        ByteUtil.addBytes(message, bytes, 1);
        ByteUtil.addBytes(message, new byte[]{(byte) new Date().getHours()}, 3);

        byte xor = CommonCheckUtil.getXor(ByteUtil.subBytes(message, 0, 4));
        message[4] = xor;

        ElfinMessageHeader messageHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
        return new ThsElfinMessage(message, messageHeader);
    }

    @Override
    public ElfinType protocolType() {
        return ElfinType.Air_Set_Model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
