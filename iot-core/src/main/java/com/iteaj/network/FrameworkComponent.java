package com.iteaj.network;

public interface FrameworkComponent {
    /**
     * 组件功能名称
     * @return
     */
    String name();

    ProtocolFactory protocolFactory();

    ProtocolTimeoutStorage protocolTimeoutStorage();

    Class<? extends AbstractMessage> messageClass();
}
