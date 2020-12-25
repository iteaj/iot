package com.iteaj.iot.client;

import com.iteaj.network.ProtocolException;

public class ClientProtocolException extends ProtocolException {

    public ClientProtocolException(Object protocolType) {
        super(protocolType);
    }

    public ClientProtocolException(String message) {
        super(message);
    }

    public ClientProtocolException(String message, Object protocolType) {
        super(message, protocolType);
    }

    public ClientProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientProtocolException(Throwable cause) {
        super(cause);
    }

    public ClientProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
