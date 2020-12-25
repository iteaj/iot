package com.iteaj.network;

import com.iteaj.network.business.BusinessFactory;

/**
 * Create Date By 2017-09-14
 * @author iteaj
 * @since 1.7
 */
public interface BusinessAction {

    /**
     * 业务执行
     * @param factory 业务工厂
     */
    AbstractProtocol exec(BusinessFactory factory);

    /**
     * 业务执行
     * 执行设备主动请求的协议或者设备响应的协议所需要处理的业务
     * 1.如果是设备主动请求的协议则执行完业务之后需要响应设备, {@link AbstractProtocol}就是响应设备的协议报文
     * @param business 协议业务
     */
    AbstractProtocol exec(ProtocolHandle business);

}
