package com.iteaj.network.server;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.FrameworkComponent;
import com.iteaj.network.ProtocolException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来存储和获取设备组件{@link ServerComponent}
 * @see #getByPort(Integer) 通过端口获取
 * @see #getByClass(Class) 通过报文类型{@link AbstractMessage}获取
 * @see #getServerComponents() 获取TCP端口的设备组件列表
 * @see #getUdpServerComponents() 获取UDP端口的设备组件列表
 */
public class ServerComponentFactory implements InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    private List<DeviceServerComponent> serverComponents = new ArrayList<>();
    private List<DeviceUdpServerComponent> udpServerComponents = new ArrayList<>();
    private Map<Integer, ServerComponent> componentMap = new HashMap<>(8);
    private Map<Class<? extends AbstractMessage>, ServerComponent> messageComponentMap = new HashMap(8);

    public List<DeviceServerComponent> getServerComponents() {
        return this.serverComponents;
    }

    public List<DeviceUdpServerComponent> getUdpServerComponents() {
        return this.udpServerComponents;
    }

    public ServerComponent getByPort(Integer port) {
        return componentMap.get(port);
    }

    public ServerComponent getByClass(Class<? extends AbstractMessage> messageClass) {
        return messageComponentMap.get(messageClass);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final ObjectProvider<ServerComponent> beanProvider = this.beanFactory.getBeanProvider(ServerComponent.class);

        beanProvider.stream().forEach(component -> {
            IotDeviceServer deviceServer = component.deviceServer();
            if(null == deviceServer) {
                throw new IllegalArgumentException("未指定设备服务对象: DeviceServerComponent.deviceServer()");
            }

            if(component instanceof DeviceServerComponent) {
                serverComponents.add((DeviceServerComponent) component);
            }

            if(component instanceof DeviceUdpServerComponent) {
                udpServerComponents.add((DeviceUdpServerComponent) component);
            }

            int port = deviceServer.port();
            if(port <= 0 || port > 65535)
                throw new BeanInitializationException("服务端组件: " + deviceServer.name() + "使用错误的端口: " + port);

            // 已经有组件使用此端口, 抛出异常
            final ServerComponent serverComponent = componentMap.get(port);
            if(serverComponent != null) {
                throw new BeanInitializationException(serverComponent.deviceServer().name()
                        + "和" + deviceServer.name() + "使用同一个端口: " + deviceServer.port());
            }

            // 每种设备组件的报文类型不能为空, 且不能和其他的设备组件的报文类型不能同一类型
            Class<? extends AbstractMessage> messageClass = component.messageClass();
            if(messageClass == null) {
                throw new BeanInitializationException("请指定组件的报文类型<M>："
                        + component.getClass().getSimpleName()+"<M>");
            }

            componentMap.put(port, component);
            messageComponentMap.put(messageClass, component);
        });
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
