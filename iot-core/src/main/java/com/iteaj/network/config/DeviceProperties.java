package com.iteaj.network.config;

public class DeviceProperties {

    /**
     * 端口
     */
    private Integer port;

    /**
     * 没读和没写多长时间判断失活
     */
    private long allIdleTime = 90;

    /**
     * 多久没读判断失活的时间
     */
    private long readerIdleTime = 0;

    /**
     * 多久没写判断失活的时间
     */
    private long writerIdleTime = 0;

    public DeviceProperties() { }

    public DeviceProperties(Integer port) {
        this.port = port;
    }

    public DeviceProperties(Integer port, long allIdleTime, long readerIdleTime, long writerIdleTime) {
        this.port = port;
        this.allIdleTime = allIdleTime;
        this.readerIdleTime = readerIdleTime;
        this.writerIdleTime = writerIdleTime;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public long getAllIdleTime() {
        return allIdleTime;
    }

    public void setAllIdleTime(long allIdleTime) {
        this.allIdleTime = allIdleTime;
    }

    public long getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(long readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    public long getWriterIdleTime() {
        return writerIdleTime;
    }

    public void setWriterIdleTime(long writerIdleTime) {
        this.writerIdleTime = writerIdleTime;
    }
}
