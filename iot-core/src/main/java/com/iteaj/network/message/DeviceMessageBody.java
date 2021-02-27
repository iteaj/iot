package com.iteaj.network.message;

import com.iteaj.network.Message;

/**
 * <p>设备报文体</p>
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public class DeviceMessageBody implements Message.MessageBody {

    private byte[] message;

    public DeviceMessageBody(byte[] message) {
        this.message = message;
    }

    @Override
    public int getBodyLength() {
        return message.length;
    }

    @Override
    public byte[] getBodyMessage() {
        return message;
    }

}
