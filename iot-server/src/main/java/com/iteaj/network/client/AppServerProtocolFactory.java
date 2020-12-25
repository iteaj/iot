package com.iteaj.network.client;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.ProtocolFactory;
import com.iteaj.network.ProtocolTimeoutStorage;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppClientType;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;
import com.iteaj.network.server.protocol.NoneDealProtocol;

/**
 * 用于创建应用程序客户端协议
 * @see AppClientServerProtocol
 * @see AppClientServerHandle
 */
public class AppServerProtocolFactory extends ProtocolFactory<AppClientMessage> {

    public AppServerProtocolFactory(ProtocolTimeoutStorage protocolTimeoutStorage) {
        super(protocolTimeoutStorage);
    }

    @Override
    public AbstractProtocol getProtocol(AppClientMessage message) {
        if(message.getClientType() == AppClientType.App_Client_Heart) {
            return NoneDealProtocol.getInstance(message.getHead().getEquipCode());
        } else {
            try {
                DeviceRequestProtocol requestProtocol = new AppClientServerProtocol(message).buildRequestMessage();
                /**
                 * 此处设置执行状态为 null, 为了判断是否需要直接响应客户端还是等待拿到设备报文之后在响应客户端
                 * @see AppClientServerProtocol#doBuildResponseMessage()
                 */
                requestProtocol.setExecStatus(null);
                return requestProtocol;
            } catch (Exception e) {
                DeviceRequestProtocol requestMessage = new AppClientServerProtocol(message).setFailEx(e).buildRequestMessage();
                requestMessage.setExecStatus(ExecStatus.失败); // 设置执行状态未失败, 将直接响应失败状态给客户端

                // 构建响应报文
                return requestMessage;
            }
        }
    }
}
