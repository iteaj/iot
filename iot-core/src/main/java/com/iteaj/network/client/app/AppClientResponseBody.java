package com.iteaj.network.client.app;

import com.iteaj.network.consts.ExecStatus;

/**
 * 应用程序客户端响应报文
 */
public class AppClientResponseBody implements AppClientMessageBody {

    // 问题描述
    private String reason;
    // 响应状态
    private ExecStatus status;

    public AppClientResponseBody(String reason, ExecStatus status) {
        this.reason = reason;
        this.status = status;
    }

    public ExecStatus getStatus() {
        return status;
    }

    public void setStatus(ExecStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
