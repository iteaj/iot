package com.iteaj.network;

/**
 * <p>协议对应的业务处理器</p>
 * Create Date By 2020-09-21
 * @author iteaj
 * @since 1.8
 */
public interface ProtocolHandle<T extends Protocol> {

    /**
     * 响应时调用各协议业务执行
     * @param protocol
     */
    Object business(T protocol);

    /**
     * 注册此协议业务对应的协议类型
     * @return
     */
    Object protocolType();
}
