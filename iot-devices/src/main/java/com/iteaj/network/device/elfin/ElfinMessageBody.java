package com.iteaj.network.device.elfin;

import com.iteaj.network.Message;

public class ElfinMessageBody implements Message.MessageBody {

    private byte[] messageBody;

    public ElfinMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public int getBodyLength() {
        return messageBody.length;
    }

    @Override
    public byte[] getBodyMessage() {
        return this.messageBody;
    }
}
