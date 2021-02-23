package com.iteaj.network;

/**
 * create time: 2021/2/21
 *  设备协议工厂, 用来生产协议对象
 * @author iteaj
 * @since 1.0
 */
public interface DeviceProtocolFactory<T extends AbstractMessage> extends StorageManager<String, Protocol>{

    /**
     * 获取协议
     * @param message
     * @return
     */
    AbstractProtocol getProtocol(T message);
}
