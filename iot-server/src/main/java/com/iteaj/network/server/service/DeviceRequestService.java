package com.iteaj.network.server.service;

import com.iteaj.network.server.ServerProtocolHandle;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;

/**
 * <p>设备请求平台需要执行的业务</p>
 * @see DeviceRequestProtocol 此基类下的所有子协议都使用此业务
 * Create Date By 2017-09-21
 * @author iteaj
 * @since 1.7
 */
@Deprecated
public interface DeviceRequestService<T extends DeviceRequestProtocol> extends ServerProtocolHandle<T> {

    @Override
    default Object business(T protocol) {
        return doBusiness(protocol);
    }

    Object doBusiness(T protocol);

}
