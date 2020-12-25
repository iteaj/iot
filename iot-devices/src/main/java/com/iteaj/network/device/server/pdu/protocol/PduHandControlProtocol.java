package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.device.server.pdu.PduMessageUtil;
import com.iteaj.network.device.server.pdu.PduTradeType;

import java.io.IOException;

public class PduHandControlProtocol extends PduPlatformProtocol{
    /**
     * 'Reboot'
     * 'DelayOpen'
     * 'DelayClose'
     * 'DelayReboot'
     */
    private String type;
    /**
     * 如要控制第三个口时，state=4 即 0000 0100 也即 2 的(3-1)次方
     */
    private String state;

    private int delayTime; //延时时间，1-999，单位秒

    private static final String content = "START handcontrol tag='%s' state='%s' type='%s' check='%s' END\n";
    private static final String delayContent = "START handcontrol tag='%s' state='%s' type='%s' delaytime='%s' check='%s' END\n";

    public PduHandControlProtocol(String equipCode
            , String type, String state, int delayTime) {
        super(equipCode);
        this.type = type;
        this.state = state;
        this.delayTime = delayTime;
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.handcontrol;
    }

    @Override
    protected AbstractMessage doBuildRequestMessage() throws IOException {
        String message;
        if("Reboot".equals(type)) {
            message = String.format(content, getMessageId(), state, type, null);
            int code = PduMessageUtil.getCode(message);
            message = String.format(content, getMessageId(), state, type, code);
        } else {
            message = String.format(delayContent, getMessageId(), state, type, delayTime, null);
            int code = PduMessageUtil.getCode(message);
            message = String.format(delayContent, getMessageId(), state, type, delayTime, code);
        }

        return newMessage(message.getBytes("GBK"));
    }

    @Override
    protected AbstractMessage resolverResponseMessage(AbstractMessage message) {
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }
}
