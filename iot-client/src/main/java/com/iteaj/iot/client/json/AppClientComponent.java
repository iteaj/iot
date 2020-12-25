package com.iteaj.iot.client.json;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.ClientProperties;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.client.ClientMessage;
import com.iteaj.network.client.app.AppClientMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class AppClientComponent extends ClientComponent {

    @Autowired
    private ClientProperties properties;

    @Override
    protected IotNettyClient createNettyClient() {
        ClientProperties.AppClient app = properties.getApp();
        return new IotAppClient(app.getHost(), app.getPort(), this);
    }

    @Override
    protected ProtocolFactory createProtocolFactory() {
        return new AppClientFactory(protocolTimeoutStorage());
    }

    @Override
    public String name() {
        return "应用程序客户端";
    }

    @Override
    public Class<? extends ClientMessage> messageClass() {
        return AppClientMessage.class;
    }
}
