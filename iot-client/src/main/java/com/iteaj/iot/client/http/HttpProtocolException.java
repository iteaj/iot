package com.iteaj.iot.client.http;

import com.iteaj.network.ProtocolException;

public class HttpProtocolException extends ProtocolException {

    public HttpProtocolException(Object protocolType) {
        super(protocolType);
    }

    public HttpProtocolException(String message) {
        super(message);
    }

    public HttpProtocolException(String message, Object protocolType) {
        super(message, protocolType);
    }

    public HttpProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpProtocolException(Throwable cause) {
        super(cause);
    }

    public HttpProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
