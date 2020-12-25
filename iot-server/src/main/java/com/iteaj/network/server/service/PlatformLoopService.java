package com.iteaj.network.server.service;

import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.message.LocalLoopMessage;
import com.iteaj.network.server.protocol.AbstractLoopProtocol;
import com.iteaj.network.ProtocolHandle;

/**
 * <p>平台回环协议业务对象实体</p>
 * @see LocalLoopMessage
 * @see AbstractLoopProtocol
 * Create Date By 2017-09-29
 * @author iteaj
 * @since 1.7
 */
public abstract class PlatformLoopService<T extends AbstractLoopProtocol>
        extends PlatformRequestService<T> implements ProtocolHandle<T> {

    @Override
    public Object success(T protocol) {
        throw new UnsupportedOperationException("不支持此操作");
    }

    @Override
    public Object failed(T protocol, ExecStatus status) {
        throw new UnsupportedOperationException("不支持此操作");
    }

    @Override
    public Object exception(T protocol, Throwable e) {
        throw new UnsupportedOperationException("不支持此操作");
    }

}
