package com.iteaj.network;

public class ProtocolWrap implements Protocol {
    private long timeout;
    private long startTime;
    private Protocol protocol;

    public ProtocolWrap(long startTime, long timeout, Protocol protocol) {
        this.timeout = timeout;
        this.protocol = protocol;
        this.startTime = startTime;
    }

    @Override
    public <T> T protocolType() {
        return protocol.protocolType();
    }

    public long getTimeout() {
        return timeout;
    }

    @Override
    public String desc() {
        return protocol.desc();
    }

    @Override
    public String getEquipCode() {
        return protocol.getEquipCode();
    }

    @Override
    public Message requestMessage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Message responseMessage() {
        throw new UnsupportedOperationException();
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public Protocol addParam(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Protocol removeParam(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getParam(String key) {
        return this.protocol.getParam(key);
    }
}
