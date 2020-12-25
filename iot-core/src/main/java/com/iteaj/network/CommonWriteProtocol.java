package com.iteaj.network;

import com.iteaj.network.business.BusinessFactory;

/**
 * 通用写协议
 */
public class CommonWriteProtocol extends AbstractProtocol<AbstractMessage> {

    public CommonWriteProtocol(AbstractMessage writeMessage) {
        super(writeMessage, writeMessage);
    }

    @Override
    public AbstractProtocol buildRequestMessage() {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public AbstractProtocol buildResponseMessage() {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public boolean isRelation() {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public Object relationKey() {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public <T> T protocolType() {
        throw new UnsupportedOperationException("不支持操作");
    }
}
