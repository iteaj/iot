package com.iteaj.network;

/**
 * 用来声明和设备对接时用来管理设备的设备组件
 */
public interface FrameworkComponent {
    /**
     * 组件功能名称
     * @return
     */
    String name();

    /**
     * 设备相关协议的协议工厂
     * @return
     */
    DeviceProtocolFactory protocolFactory();

    /**
     * 用来处理同步和异步协议的存储
     * @see AbstractProtocolTimeoutManager
     * @return
     */
    ProtocolTimeoutStorage protocolTimeoutStorage();

    /**
     * 对应设备的报文Class对象
     * @see AbstractMessage
     * @return
     */
    Class<? extends AbstractMessage> messageClass();
}
