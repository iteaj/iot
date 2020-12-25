package com.iteaj.network.device.client.breaker.fzwu;

import com.iteaj.network.message.MqttClientMessage;

public class BreakerMessage extends MqttClientMessage {

    private String header; // 开始符
    private short len; // 长度
    private String ctrl; // 控制域
    private String addr; // 地址域
    private String type; // 功能码.
    private byte[] data; // 数据域
    private String time; // 数据时效
    private String cs; // 效检
    private String over; // 终止符

    private String gateway; // 断路器网关

    @Override
    public String getDeviceSn() {
        return this.getAddr();
    }

    public BreakerMessage() {
        this(null);
    }

    public BreakerMessage(String topic) {
        super(new byte[0], topic);
    }

    public BreakerMessage(byte[] message, String topic) {
        super(message, topic);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public short getLen() {
        return len;
    }

    public void setLen(short len) {
        this.len = len;
    }

    public String getCtrl() {
        return ctrl;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    @Override
    public String toString() {
        return "BreakerMessage{" +
                "header='" + header + '\'' +
                ", len=" + len +
                ", ctrl='" + ctrl + '\'' +
                ", addr='" + addr + '\'' +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", cs='" + cs + '\'' +
                ", over='" + over + '\'' +
                '}';
    }
}
