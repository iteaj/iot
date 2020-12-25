package com.iteaj.iot.client.mqtt;

import com.iteaj.iot.client.ServerRequestClientService;

/**
 * Matt broker 推送的业务处理
 * @param <T>
 */
public abstract class MqttBrokerRequestService<T extends MqttBrokerRequestProtocol> implements ServerRequestClientService<T> {

}
