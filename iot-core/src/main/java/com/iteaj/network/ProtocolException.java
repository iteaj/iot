package com.iteaj.network;

/**
 * Create Date By 2017-09-08
 *
 * @author iteaj
 * @since 1.7
 */
public class ProtocolException extends RuntimeException{

    private Object protocolType;

    public ProtocolException(Object protocolType) {
        this.protocolType = protocolType;
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, Object protocolType) {
        super(message);
        this.protocolType = protocolType;
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolException(Throwable cause) {
        super(cause);
    }

    public ProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Object getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(Object protocolType) {
        this.protocolType = protocolType;
    }
}
