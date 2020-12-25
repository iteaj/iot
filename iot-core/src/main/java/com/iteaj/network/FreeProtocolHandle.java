package com.iteaj.network;

/**
 * 自定义协议处理器
 */
public interface FreeProtocolHandle<T extends Protocol> extends ProtocolHandle<T> {

    @Override
    default ProtocolType protocolType() {
        return null;
    }
}
