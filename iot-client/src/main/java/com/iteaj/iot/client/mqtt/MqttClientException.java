package com.iteaj.iot.client.mqtt;

import com.iteaj.iot.client.ClientProtocolException;

public class MqttClientException extends ClientProtocolException {

    public MqttClientException(Object protocolType) {
        super(protocolType);
    }

    public MqttClientException(String message) {
        super(message);
    }

    public MqttClientException(String message, Object protocolType) {
        super(message, protocolType);
    }

    public MqttClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqttClientException(Throwable cause) {
        super(cause);
    }

    public MqttClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
