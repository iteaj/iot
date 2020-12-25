package com.iteaj.network.server;

import com.iteaj.network.AbstractMessage;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerComponentFactory implements InitializingBean {

    @Autowired
    private List<DeviceServerComponent> serverComponents;
    private Map<Integer, DeviceServerComponent> componentMap = new HashMap<>(8);
    private Map<Class<? extends AbstractMessage>, DeviceServerComponent> messageComponentMap = new HashMap(8);

    public List<DeviceServerComponent> getServerComponents() {
        return this.serverComponents;
    }

    public DeviceServerComponent getByPort(Integer port) {
        return componentMap.get(port);
    }

    public DeviceServerComponent getByClass(Class<? extends AbstractMessage> messageClass) {
        return messageComponentMap.get(messageClass);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(CollectionUtils.isEmpty(serverComponents))
            throw new BeanInitializationException("至少启用一个服务端组件来进行端口监听: " + DeviceServerComponent.class);

        for(int i=0; i<serverComponents.size(); i++) {
            DeviceServerComponent serverComponent = serverComponents.get(i);
            int port = serverComponent.deviceServer().port();
            if(port <= 0 || port > 65535)
                throw new BeanInitializationException("服务端组件: " + serverComponent.deviceServer().name() + "使用错误的端口: " + port);

            // 已经有组件使用此端口, 抛出异常
            DeviceServerComponent component = componentMap.get(port);
            if(component != null) {
                throw new BeanInitializationException(serverComponent.deviceServer().name()
                        + "和" + component.deviceServer().name() + "使用同一个端口: " + component.deviceServer().port());
            }

            componentMap.put(port, serverComponent);
            messageComponentMap.put(serverComponent.messageClass(), serverComponent);
        }
    }
}
