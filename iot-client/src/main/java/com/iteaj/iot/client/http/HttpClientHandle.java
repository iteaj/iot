package com.iteaj.iot.client.http;

import com.iteaj.network.client.ClientProtocolHandle;

/**
 * 基于http实现的协议的处理器
 * @param <T>
 */
public interface HttpClientHandle<T extends HttpClientProtocol> extends ClientProtocolHandle<T> {

}
