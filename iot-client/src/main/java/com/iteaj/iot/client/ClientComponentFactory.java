package com.iteaj.iot.client;

import com.iteaj.network.client.ClientMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientComponentFactory implements InitializingBean {

    private List<ClientComponent> clientComponents;
    @Autowired
    private ObjectProvider<List<ClientComponent>> objectProvider;
    private Map<Class<? extends ClientMessage>, ClientComponent> messageComponentMap = new HashMap(8);

    public List<ClientComponent> getComponents() {
        return this.clientComponents;
    }

    public ClientComponent getByClass(Class<? extends ClientMessage> messageClass) {
        return messageComponentMap.get(messageClass);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        objectProvider.ifAvailable(item -> {
            clientComponents = item;
            for(int i=0; i<clientComponents.size(); i++) {
                ClientComponent serverComponent = clientComponents.get(i);

                messageComponentMap.put(serverComponent.messageClass(), serverComponent);
            }
        });

        if(CollectionUtils.isEmpty(clientComponents))
            this.clientComponents = new ArrayList<>();
    }

}
