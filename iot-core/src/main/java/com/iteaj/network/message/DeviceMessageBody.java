package com.iteaj.network.message;

import com.iteaj.network.Message;

/**
 * <p>设备报文体</p>
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public class DeviceMessageBody implements Message.MessageBody {

    private int length;
    private byte[] messageBody;

    public DeviceMessageBody(byte[] messageBody) {
        this(messageBody, messageBody.length);
    }

    public DeviceMessageBody(byte[] messageBody, int length) {
        this.length = length;
        this.messageBody = messageBody;
    }

    @Override
    public int getBodyLength() {
        return length;
    }

    @Override
    public byte[] getBodyMessage() {
        return messageBody;
    }


}
