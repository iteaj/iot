package com.iteaj.network.server.service;

import com.iteaj.network.ProtocolType;
import com.iteaj.network.server.ServerProtocolHandle;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;

/**
 * create time: 2021/3/6
 *
 * 用来处理平台请求协议的业务
 *
 * @see PlatformRequestProtocol
 * @author iteaj
 * @since 1.0
 */
public interface PlatformRequestHandle<T extends PlatformRequestProtocol> extends ServerProtocolHandle<T> {

    @Override
    @Deprecated
    default ProtocolType protocolType() {
        return null;
    }
}
