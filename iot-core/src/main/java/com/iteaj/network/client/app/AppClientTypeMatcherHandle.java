package com.iteaj.network.client.app;

import com.iteaj.network.client.ClientRelation;

/**
 * 用于处理设备服务端处理
 * @param <T>
 */
public interface AppClientTypeMatcherHandle<T extends AppClientMessageBody> {

    /**
     * 如果交易类型匹配, 则客户端请求将交由此handle处理协议请求 {@link #handle(AppClientMessage)}
     * @see AppClientMessageHead#getTradeType()
     * @param tradeType
     * @return
     */
    boolean isMatcher(String tradeType);

    /**
     * 反序列化报文体
     * @see RequestType 报文通过此字段判断是请求还是响应
     * @see AppClientMessageBody
     * @see AppClientResponseBody 服务端响应的反序列化
     * @return
     */
    T deserialize(Object body, RequestType type);

    /**
     * 报文处理
     * @see com.iteaj.network.client.ClientRelation
     * @see com.iteaj.network.client.ClientRelationEntity
     * @param message 应用程序客户端提交的报文
     * @return 如果有返回值将等待设备响应之后在响应客户端, 如果没有返回值将直接响应客户端
     */
    ClientRelation handle(AppClientMessage<T> message);
}
