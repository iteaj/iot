package com.iteaj.network.server.service;

import com.iteaj.network.ProtocolType;
import com.iteaj.network.server.ServerProtocolHandle;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;

/**
 * create time: 2021/3/6
 *
 * 用来处理设备请求协议类型的业务处理
 *
 * @see DeviceRequestProtocol
 * @author iteaj
 * @since 1.0
 */
public interface DeviceRequestHandle<T extends DeviceRequestProtocol> extends ServerProtocolHandle<T> {

    @Override
    Object business(T protocol);

    @Override
    @Deprecated
    default ProtocolType protocolType() {
        return null;
    }
}
