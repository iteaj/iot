package com.iteaj.iot.client.json;

import com.iteaj.iot.client.ClientRequestProtocol;
import com.iteaj.iot.client.IotClientBootstrap;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.network.client.app.*;
import com.iteaj.network.protocol.CommonProtocolType;

/**
 * 应用程序客户端和服务端通信的协议
 */
public class AppClientProtocol extends ClientRequestProtocol<AppClientMessage> {

    /**
     * 执行状态说明
     */
    private String reason;

    protected AppClientProtocol(AppClientMessage message) {
        this.requestMessage = message;
    }

    @Override
    public AppClientProtocol buildRequestMessage() {
        if(getTimeout() > 0) { // 如果设置超时时间
            AppClientMessageHead head = this.requestMessage.getHead();
            head.setTimeout(this.getTimeout());
        }

        String toJSONString = AppClientUtil.buildByteMessage(this.requestMessage);

        if(logger.isDebugEnabled()) {
            logger.debug("应用客户端协议构建报文 - 报文: {}", toJSONString);
        }

        return this;
    }

    public static AppClientProtocol buildRequestProtocol(AppClientMessage message) {
        return new AppClientProtocol(message);
    }

    @Override
    public void doBuildResponseMessage(AppClientMessage message) {
        AppClientResponseBody body = (AppClientResponseBody)message.getBody();

        this.reason = body.getReason();
        this.setExecStatus(body.getStatus());
    }

    @Override
    protected IotNettyClient getIotNettyClient() {
        return IotClientBootstrap.getClient(AppClientMessage.class);
    }

    @Override
    public CommonProtocolType protocolType() {
        return CommonProtocolType.TCClint;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
