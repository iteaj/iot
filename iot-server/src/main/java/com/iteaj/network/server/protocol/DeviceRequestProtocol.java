package com.iteaj.network.server.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.Message;
import com.iteaj.network.ProtocolHandle;
import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.server.AbstractServerProtocol;

/**
 * <p>设备主动发请求到平台所有协议的基类</p>
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public abstract class DeviceRequestProtocol<M extends AbstractMessage> extends AbstractServerProtocol<M> {

    public DeviceRequestProtocol(M requestMessage) {
        super(requestMessage, null);
    }

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        return this.exec(factory.getProtocolHandle(getClass()));
    }

    /**
     * 构建响应报文
     * @return
     */
    public DeviceRequestProtocol buildResponseMessage() {
        //如果报文存在,清空重新创建
        if(null != responseMessage()) responseMessage = null;

        //对于设备主动发起的请求,必须包含请求报文
        if(null == requestMessage())
            throw new IllegalStateException("不存在请求报文："+ requestMessage());

        //创建响应报文
        M t = doBuildResponseMessage();

        // 如果没有返回响应报文, 则无需响应给设备(客户端)
        if(t != null) {
            responseMessage =  t;
            return this;
        } else {
            return null;
        }
    }

    protected abstract M doBuildResponseMessage();

    /**
     * 对请求平台的报文进行报文解析
     * @return
     */
    public DeviceRequestProtocol buildRequestMessage() {
        resolverRequestMessage(this.requestMessage());

        Message.MessageHead messageHead = this.requestMessage().getHead();
        if(logger.isDebugEnabled()) {
            logger.debug("请求平台协议 - 协议类型：{} - 设备编号：{} - 报文头：{} - 报文体：{}"
                    , protocolType(), getEquipCode(), messageHead.toString(), this.toString());
        }

        return this;
    }

    protected abstract void resolverRequestMessage(M requestMessage);

    /**
     * 设备主动请求的协议无需存储对应关系
     * @return
     */
    @Override
    public boolean isRelation() {
        return false;
    }

    /**
     * @see #isRelation() 由于无需存储对应关系所以无需key
     * @return
     */
    @Override
    public Object relationKey() {
        return null;
    }

    /**
     * 执行设备请求业务
     * @param business 协议业务
     */
    @Override
    public AbstractProtocol exec(ProtocolHandle business) {
        //执行业务
        if(null != business) {
            super.exec(business);
        } else {
            if(logger.isWarnEnabled()) {
                logger.debug("未指定业务类型 协议类型: {} - 协议描述: {} - 说明: 此协议将无业务处理"
                        , getClass().getSimpleName(), desc());
            }
        }

        /**
         * 构建响应报文
         * 1. 有返回: 返回的报文将响应给设备
         * 2. 无返回: 将不响应设备
         */
        return buildResponseMessage();
    }

    @Override
    public M requestMessage() {
        return (M) super.requestMessage();
    }

    @Override
    public M responseMessage() {
        return (M) super.responseMessage();
    }
}
