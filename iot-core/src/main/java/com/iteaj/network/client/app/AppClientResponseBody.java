package com.iteaj.network.client.app;

import com.iteaj.network.client.ClientRelationEntity;
import com.iteaj.network.consts.ExecStatus;

/**
 * 应用程序客户端响应报文体
 * @see AppClientMessage#getBody()
 * @see AppClientUtil#buildServerResponseMessage(AppClientMessageHead, AppClientResponseBody)
 * @see AppClientUtil#buildServerResponseMessage(ClientRelationEntity, AppClientResponseBody)
 */
public class AppClientResponseBody implements AppClientMessageBody {

    // 响应的数据
    private Object data;
    // 问题描述
    private String reason;
    // 响应状态
    private ExecStatus status;

    public AppClientResponseBody(String reason, ExecStatus status) {
        this.reason = reason;
        this.status = status;
    }

    public AppClientResponseBody(Object data, String reason, ExecStatus status) {
        this.data = data;
        this.reason = reason;
        this.status = status;
    }

    public static AppClientResponseBody success(String reason) {
        return new AppClientResponseBody(reason, ExecStatus.成功);
    }

    public static AppClientResponseBody success() {
        return success("OK");
    }

    public ExecStatus getStatus() {
        return status;
    }

    public AppClientResponseBody setStatus(ExecStatus status) {
        this.status = status;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public AppClientResponseBody setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Object getData() {
        return data;
    }

    public AppClientResponseBody setData(Object data) {
        this.data = data;
        return this;
    }
}
