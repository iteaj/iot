package com.iteaj.network.client;

import com.iteaj.network.ProtocolException;

/**
 * 用来处理应用程序客户端和服务端之间的关联关系
 * 应用客户端发起 -> 请求设备服务端 -> 请求设备 -> 设备响应 -> 响应客户端
 */
public interface ClientRelation<T extends ClientRelation> {

    /**
     * 同步执行
     * @param timeout
     * @return
     */
    T sync(long timeout);

    /**
     * 指定异步超时时间
     * @param timeout
     * @return
     */
    T timeout(long timeout);

    /**
     * 获取异步超时时间
     * @return
     */
    long getTimeout();

    /**
     * 发起请求
     * @return
     * @throws ProtocolException
     */
    T request() throws ProtocolException;

    /**
     * 用来声明此次请求是否需要加入超时管理器
     * @see com.iteaj.network.AbstractProtocolTimeoutManager
     * @return
     */
    default boolean isRelation() {
        return getTimeout() > 0;
    }

    /**
     * 此次协议是否由客户端协议发起并且调用的
     * @return
     */
    boolean isClientStart();

    T setClientStart(boolean start);

    ClientRelationEntity getClientEntity();

    void setClientEntity(ClientRelationEntity entity);

}
