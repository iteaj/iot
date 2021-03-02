package com.iteaj.network.server;

import com.iteaj.network.*;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

/**
 * 用来封装需要监听在TCP端口的设备的所需要的服务端组件
 */
public abstract class ServerComponent implements FrameworkComponent, InitializingBean {

    private IotDeviceServer iotDeviceServer;
    private DeviceProtocolFactory protocolFactory;
    private ProtocolTimeoutStorage protocolTimeoutManager;


    @Override
    public String name() {
        return deviceServer().name();
    }

    /**
     * 创建设备服务端
     * @return
     */
    public final IotDeviceServer deviceServer() {
        if(this.iotDeviceServer != null)
            return this.iotDeviceServer;

        this.iotDeviceServer = createDeviceServer();
        if(this.iotDeviceServer == null)
            throw new IllegalStateException("未指定设备服务端对象: " + IotDeviceServer.class.getName() + "在设备组件对象中: " + getClass().getSimpleName());

        return iotDeviceServer;
    }

    protected abstract IotDeviceServer createDeviceServer();

    /**
     * 创建协议工厂
     * @return
     */
    public final DeviceProtocolFactory protocolFactory() {
        if(this.protocolFactory != null) {
            return this.protocolFactory;
        }

        this.protocolFactory = createProtocolFactory();
        if(this.protocolFactory == null) {
            throw new BeanInitializationException("组件["+getClass().getSimpleName()+"]未创建协议工厂实例["+ProtocolFactory.class.getSimpleName()+"]");
        }

        if(this.protocolFactory instanceof DeviceProtocolFactory) {
            ProtocolFactory protocolFactory = (ProtocolFactory) this.protocolFactory;
            if (protocolFactory.getDelegation() == null) {
                protocolFactory.setDelegation(protocolTimeoutStorage());
            }
        }

        return this.protocolFactory;
    }

    protected abstract DeviceProtocolFactory createProtocolFactory();

    /**
     * 设备服务端使用的报文类
     * 此类将用来标识唯一的设备服务组件
     * @return
     */
    public abstract Class<? extends AbstractMessage> messageClass();

    public final ProtocolTimeoutStorage protocolTimeoutStorage() {
        if(protocolTimeoutManager != null) {
            return this.protocolTimeoutManager;
        }

        return this.protocolTimeoutManager = doCreateProtocolTimeoutStorage();
    }

    /**
     * 创建超时管理器
     * @return
     */
    protected ProtocolTimeoutStorage doCreateProtocolTimeoutStorage() {
        IotDeviceServer iotDeviceServer = this.deviceServer();

        return new ProtocolTimeoutStorage(iotDeviceServer.name());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        deviceServer();
        protocolFactory();
        protocolTimeoutStorage();
    }

}
