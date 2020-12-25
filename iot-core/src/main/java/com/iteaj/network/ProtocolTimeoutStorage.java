package com.iteaj.network;

import org.apache.commons.lang3.StringUtils;

/**
 * 协议超时存储, 用来存储请求客户端的协议, 等客户端响应时移除
 */
public class ProtocolTimeoutStorage extends ConcurrentStorageManager<String, Protocol> {

    // 超时管理描述
    private String desc;
    public ProtocolTimeoutStorage(String desc) {
        super();
        this.desc = desc;
    }

    @Override
    public Protocol get(String key) {
        Protocol protocol = super.get(key);
        if(protocol != null)
            return ((ProtocolWrap) protocol).getProtocol();

        return null;
    }

    @Override
    public Protocol add(String key, Protocol val) {
        throw new UnsupportedOperationException("不支持此操作");
    }

    public Protocol add(String key, Protocol protocol, long timeout) {
        if(StringUtils.isBlank(key) || null == protocol) {
            throw new IllegalArgumentException("参数错误");
        }

        if(timeout <= 0) {
            throw new IllegalArgumentException("请指定正确的超时时间");
        }

        if(isExists(key)) {
            throw new IllegalStateException("设备端: " + getDesc() + " 协议关联标识重复: " + key);
        }

        return super.add(key, new ProtocolWrap(System.currentTimeMillis(), timeout, protocol));
    }

    @Override
    public Protocol remove(String key) {
        ProtocolWrap remove = (ProtocolWrap) super.remove(key);
        if(null != remove)
            return remove.getProtocol();

        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
