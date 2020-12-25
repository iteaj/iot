package com.iteaj.iot.client;

import com.iteaj.network.FrameworkComponent;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.ProtocolTimeoutStorage;
import com.iteaj.network.client.ClientMessage;
import org.springframework.beans.factory.InitializingBean;

public abstract class ClientComponent implements FrameworkComponent, InitializingBean {

    private IotNettyClient nettyClient;
    private ProtocolFactory protocolFactory;
    private ProtocolTimeoutStorage timeoutStorage;

    public IotNettyClient nettyClient() {
        if(nettyClient != null)
            return nettyClient;

        return this.nettyClient = createNettyClient();
    }

    protected abstract IotNettyClient createNettyClient();

    @Override
    public ProtocolFactory protocolFactory() {
        if(null != protocolFactory)
            return protocolFactory;

        this.protocolFactory = createProtocolFactory();
        if(this.protocolFactory.getDelegation() == null) {
            this.protocolFactory.setDelegation(protocolTimeoutStorage());
        }

        return this.protocolFactory;
    }

    protected abstract ProtocolFactory createProtocolFactory();

    @Override
    public ProtocolTimeoutStorage protocolTimeoutStorage() {
        if(this.timeoutStorage != null)
            return this.timeoutStorage;

        return this.timeoutStorage = createProtocolTimeoutStorage();
    }

    protected ProtocolTimeoutStorage createProtocolTimeoutStorage() {
        return new ProtocolTimeoutStorage(name());
    }

    @Override
    public abstract Class<? extends ClientMessage> messageClass();

    @Override
    public void afterPropertiesSet() throws Exception {
        nettyClient();
        protocolFactory();
        protocolTimeoutStorage();
    }
}
