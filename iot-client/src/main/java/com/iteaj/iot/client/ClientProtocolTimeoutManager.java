package com.iteaj.iot.client;

import com.iteaj.iot.client.json.AppClientProtocol;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.AbstractProtocolTimeoutManager;
import com.iteaj.network.Protocol;
import com.iteaj.network.ProtocolTimeoutStorage;
import com.iteaj.network.consts.ExecStatus;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 用来管理{@link ClientRequestProtocol#relationKey()}和{@link ClientRequestProtocol}的映射关系
 */
public class ClientProtocolTimeoutManager extends AbstractProtocolTimeoutManager {

    public ClientProtocolTimeoutManager(List<ProtocolTimeoutStorage> timeoutStorages) {
        super(timeoutStorages);
    }

    public ClientProtocolTimeoutManager(List<ProtocolTimeoutStorage> timeoutStorages, Executor executor) {
        super(timeoutStorages, executor);
    }

    @Override
    protected String protocolRemoveHandle(Protocol protocol) {
        try {
            if(protocol instanceof ClientRequestProtocol) {
                ((AbstractProtocol<?>) protocol).setExecStatus(ExecStatus.超时);
                ((ClientRequestProtocol) protocol).buildResponseMessage();

                if(protocol instanceof AppClientProtocol) {
                    ((AppClientProtocol) protocol).setReason("服务端响应超时("+((AppClientProtocol) protocol).getTimeout()+"s)");
                }

                return protocol.getEquipCode();
            }

            return null;
        } finally {
            // 释放锁
            if(protocol instanceof AbstractProtocol) {
                ((AbstractProtocol) protocol).releaseLock();
            }
        }
    }
}
