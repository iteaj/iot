package com.iteaj.network;

/**
 * 协议类型
 */
public interface ProtocolType {

    /**
     * 返回协议类型
     * @return
     */
    Enum getType();

    /**
     * 返回协议描述
     * @return
     */
    String getDesc();
}
