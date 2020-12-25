package com.iteaj.network.client.app;

import com.iteaj.network.Message;

import java.beans.Transient;

public interface AppClientMessageBody extends Message.MessageBody {

    static AppJsonMessageBody jsonMessageBody() {
        return new AppJsonMessageBody();
    }

    static AppJsonMessageBody jsonMessageBody(String key, Object value) {
        return new AppJsonMessageBody().add(key, value);
    }

    @Override
    @Transient
    default int getBodyLength() {
        return 0;
    }

    @Override
    @Transient
    default byte[] getBodyMessage() {
        return new byte[0];
    }
}
