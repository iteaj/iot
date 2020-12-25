package com.iteaj.network.device.server.ths;

import cn.hutool.core.date.DateUtil;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.consts.SwitchStatus;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.utils.CommonCheckUtil;

import java.io.IOException;
import java.util.Date;

import static com.iteaj.network.device.elfin.ElfinType.Air_Switch_status;

/**
 *  红外控制空调 - 开关协议
 * @see ThsAirSetModel 设定空调型号的协议必须先调用
 */
public class ThsAirStatusSwitch extends ThsAirProtocolAbstract {

    private SwitchStatus switchStatus;

    public ThsAirStatusSwitch(String equipCode, SwitchStatus switchStatus) {
        super(equipCode);
        this.switchStatus = switchStatus;
    }

    @Override
    protected ThsElfinMessage doBuildRequestMessage() throws IOException {
        if(getSwitchStatus() == null) {
            throw new ProtocolException("请指定空调状态类型: on, off");
        }

        byte[] message = new byte[5];
        ByteUtil.addBytes(message, ByteUtil.hexToByte("04"), 0);
        switch (getSwitchStatus()) {
            case off: ByteUtil.addBytes(message, ByteUtil.hexToByte("00"), 1); break;
            case on: ByteUtil.addBytes(message, ByteUtil.hexToByte("FF"), 1); break;
        }

        message[2] = (byte) 0x08;
        message[3] = (byte) 0x08;

        byte xor = CommonCheckUtil.getXor(ByteUtil.subBytes(message, 0, 4));
        message[4] = xor;

        ElfinMessageHeader messageHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
        return new ThsElfinMessage(message, messageHeader);
    }

    @Override
    public ElfinType protocolType() {
        return Air_Switch_status;
    }

    public SwitchStatus getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(SwitchStatus switchStatus) {
        this.switchStatus = switchStatus;
    }
}
