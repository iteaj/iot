package com.iteaj.network.server;

import com.iteaj.network.*;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * <p>用来管理{@link PlatformRequestProtocol#relationKey()}与{@link Protocol}的映射关系</p>
 * 在报文超时后会自动剔除超时的协议
 * Create Date By 2017-09-17
 * @author iteaj
 * @since 1.7
 */
public class ServerTimeoutProtocolManager extends AbstractProtocolTimeoutManager {

    public ServerTimeoutProtocolManager(List<ProtocolTimeoutStorage> timeoutStorages) {
        super(timeoutStorages);
    }

    public ServerTimeoutProtocolManager(List<ProtocolTimeoutStorage> timeoutStorages, Executor executor) {
        super(timeoutStorages, executor);
    }

    protected String protocolRemoveHandle(Protocol protocol) {
        try {
            if(protocol instanceof PlatformRequestProtocol){
                PlatformRequestProtocol requestProtocol = ((PlatformRequestProtocol)protocol);

                requestProtocol.setExecStatus(ExecStatus.超时);

                //设置状态到超时,并执行业务
                requestProtocol.exec(IotServeBootstrap.BUSINESS_FACTORY);

                return ((PlatformRequestProtocol) protocol).getEquipCode();
            }
        } finally {
            // 释放锁
            if(protocol instanceof AbstractProtocol) {
                ((AbstractProtocol<?>) protocol).setExecStatus(ExecStatus.超时);
                ((AbstractProtocol) protocol).releaseLock();
            }
        }

        return null;
    }
}
