package com.iteaj.network.device.server.gps;

import com.iteaj.network.Message;

/**
 * create time: 2021/1/19
 *
 * @author iteaj
 * @since 1.0
 */
public class GpsMessageBody implements Message.MessageBody {

    private byte[] message;

    public GpsMessageBody(byte[] message) {
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
