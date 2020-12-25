package com.iteaj.network.client;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.ProtocolHandle;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppClientResponseBody;
import com.iteaj.network.client.app.AppClientType;
import com.iteaj.network.client.app.AppClientUtil;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;

/**
 * 用来接收应用程序客户端请求
 * 对应的业务：
 * @see AppClientServerHandle
 */
public class AppClientServerProtocol extends DeviceRequestProtocol<AppClientMessage> {

    private Exception failEx;
    public AppClientServerProtocol(AppClientMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public AbstractProtocol exec(ProtocolHandle business) {
        if(business == null) {
            throw new ProtocolException("对象: " + AppClientServerHandle.class.getSimpleName()+"没有注入SpringBeanFactory");
        } else {
            business.business(this);
        }

        return buildResponseMessage();
    }

    /**
     *
     * @return
     */
    @Override
    protected AppClientMessage doBuildResponseMessage() {

        AppClientMessage responseMessage;

        /**
         * 返回null将不直接响应客户端
         * @see #buildResponseMessage()
         */
        if(this.getExecStatus() == null) {
            return null;
        } else {
            /**
             * @see AppClientServerHandle#doBusiness(AppClientServerProtocol)
             */
            AppClientResponseBody responseBody;
            if(getExecStatus() == ExecStatus.成功) {
                responseBody = new AppClientResponseBody("OK", getExecStatus());
            } else {
                String reason = null;
                Throwable cause = getFailEx().getCause();
                if(cause != null) {
                    reason = cause.getMessage();
                }

                if(reason == null) {
                    reason = getFailEx().getMessage();
                }

                responseBody = new AppClientResponseBody(reason, getExecStatus());
            }

            responseMessage = AppClientUtil.buildServerResponseMessage(requestMessage().getHead(), responseBody);
        }

        return responseMessage;
    }

    @Override
    protected void resolverRequestMessage(AppClientMessage requestMessage) {

    }

    @Override
    public AppClientType protocolType() {
        return AppClientType.App_Client_Server;
    }

    public Exception getFailEx() {
        return failEx;
    }

    public AppClientServerProtocol setFailEx(Exception failEx) {
        this.failEx = failEx;
        return this;
    }
}
