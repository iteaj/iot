package com.iteaj.iot.client;

import com.iteaj.network.client.NettyClientHandle;

/**
 * 服务端主动向客户端发起的请求的协议的处理器
 * 比如: MQTT的Broker服务器请求客户端
 * @param <T>
 */
public interface ServerRequestClientService<T extends ServerRequestClientProtocol> extends NettyClientHandle<T> {


}
