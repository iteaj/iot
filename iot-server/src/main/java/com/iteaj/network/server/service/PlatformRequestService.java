package com.iteaj.network.server.service;

import com.iteaj.network.ProtocolException;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.ServerProtocolHandle;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>响应平台时需要的业务</p>
 * @see PlatformRequestProtocol 此基类下面的所有协议都使用此业务类型
 * Create Date By 2017-09-21
 * @author iteaj
 * @since 1.7
 */
public abstract class PlatformRequestService<T extends PlatformRequestProtocol> implements ServerProtocolHandle<T> {

    /**
     * 业务处理器日志
     */
    protected static Logger logger = LoggerFactory.getLogger("ProtocolHandle");

    @Override
    public Object business(T protocol) {
        ExecStatus execStatus = protocol.getExecStatus();

        try {
            if(execStatus == ExecStatus.成功) {
                return success(protocol);
            } else {
                return failed(protocol,  execStatus);
            }

        } catch (Exception e) {
            return exception(protocol, e);
        }
    }

    /**
     * 平台向设备发送的请求得到设备正确的响应
     * @param protocol
     */
    protected Object success(T protocol) {
        return doSuccess(protocol);
    }

    protected abstract Object doSuccess(T protocol);

    /**
     * 平台向设备发送的请求未得到设备正确的响应
     * @param protocol
     * @param status
     */
    protected Object failed(T protocol, ExecStatus status) {
        return doFailed(protocol, status);
    }

    protected Object doFailed(T protocol, ExecStatus status) {
        return null;
    }

    /**
     * 业务执行异常
     * @param protocol
     * @param e
     */
    protected Object exception(T protocol, Throwable e) {
        doException(e, protocol);
        return this;
    }

    /**
     * 异常处理
     * @param e
     * @param protocol
     * @return
     */
    protected void doException(Throwable e, T protocol) {
        logger.error("协议业务处理异常({}) 设备编号: {} - 协议状态: {} - 协议类型: {} - 异常信息: {}"
                ,e.getMessage(), protocol.getEquipCode(), protocol.getExecStatus(), protocol.protocolType(), e.getCause());

        throw new ProtocolException(e.getMessage(), e);
    }
}
