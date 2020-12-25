package com.iteaj.iot.client.mqtt.common.exception;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class SocketRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SocketRuntimeException() {
        super();
    }

    public SocketRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketRuntimeException(String message) {
        super(message);
    }

    public SocketRuntimeException(Throwable cause) {
        super(cause);
    }

}