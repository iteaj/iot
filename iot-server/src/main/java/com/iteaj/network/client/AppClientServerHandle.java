package com.iteaj.network.client;

import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppClientMessageHead;
import com.iteaj.network.client.app.AppClientType;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import com.iteaj.network.server.service.DeviceRequestHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;

/**
 * 对应的协议：
 * @see AppClientServerProtocol
 */
public class AppClientServerHandle implements DeviceRequestHandle<AppClientServerProtocol>, InitializingBean {

    private ObjectProvider<ClientHandleFactory> handleFactory;
    private Logger logger = LoggerFactory.getLogger(AppClientServerHandle.class);

    public AppClientServerHandle(ObjectProvider<ClientHandleFactory> handleFactory) {
        this.handleFactory = handleFactory;
    }

    @Override
    public Object business(AppClientServerProtocol protocol) {
        AppClientMessage serverMessage = protocol.requestMessage();

        try {
            AppClientMessageHead head = serverMessage.getHead();
            ClientHandleFactory handleFactory = this.handleFactory.getIfAvailable();

            /**
             * 如果处理完后:
             * 1. 如果返回了需要调用设备的协议, 先调用协议请求设备, 等待设备响应或在响应客户端
             * @see PlatformRequestProtocol#appClientHandle(Object) 等待设备响应后, 会主动在给客户端调用请求, 响应请求信息
             *
             * 2. 如果返回 null, 则直接响应客户端
             */
            ClientRelation abstractProtocol = handleFactory.getRelation(serverMessage);
            if(abstractProtocol != null) {
                // 用来标记此协议是否是由客户端请求发起的
                abstractProtocol.setClientStart(true);

                long timeout = abstractProtocol.getTimeout();

                /**
                 * 1. 如果应用客户端指定了waiting=true, 说明应用客户端需要等待设备的响应
                 * 2. 如果应用客户端要调用的协议[abstractProtocol]不需要同步等待, 则直接响应应用客户端成功
                 */
                ClientRelationEntity entity;
                if(head.isWaiting() && serverMessage.getDeviceSn()!= null) {
                    entity = new ClientRelationEntity(head.getTimeout(),
                            head.getTradeType(), head.getEquipCode(), head.getMessageId());

                    /**
                     * @see PlatformRequestProtocol#appClientHandle(Object)
                     */
                    abstractProtocol.setClientEntity(entity);

                    // 如果客户端请求的超时时间大于协议默认的超时时间, 使用客户端的时间
                    if(head.getTimeout() > timeout) {
                        timeout = head.getTimeout();
                    }

                }

                abstractProtocol.timeout(timeout).request();
            } else {
                // 没有返回任何协议, 则直接响应客户端成功
                protocol.setExecStatus(ExecStatus.成功);
            }

        } catch (Exception e) { // 直接响应客户端失败, 以及异常信息
            protocol.setFailEx(e);
            protocol.setExecStatus(ExecStatus.失败);
            logger.error("处理应用程序客户端请求失败", e);
        }

        return null;
    }

    @Override
    public AppClientType protocolType() {
        return AppClientType.App_Client_Server;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
