package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;

import java.math.BigDecimal;

/**
 * 断路器网关上报每台断路器的信息, 每次网关连接上来发送一次
 * 用于更新保存完整的断路器信息以及将断路器和网关进行绑定
 */
public class BreakerInfo extends BreakerDeviceRequestProtocol {

    /**
     * 断路器编号 如 SN：000012345678
     */
    private String sn;
    /**
     * 1:一代断路器
     * 2:二代断路器
     * 3:4P 三相重合闸 HX
     * 4:3P 三相重合闸 HX
     * 5:2P 断路器 HX
     * 6:1P 断路器 HX
     * 7:三相塑壳 DL
     * 8:ST 漏电重合闸 T 型（带温度）
     * 9：ST 漏电重合闸 Y 型（不带温度）
     * 10：ST 塑壳 Y 型(无重合、无漏电)
     * 11：ST 塑壳 M 型（无分合、无漏电）
     */
    private String model;
    /**
     * 硬件版本号 如 version：000012345678
     */
    private String hv;
    /**
     * 程序版本
     */
    private String pv;
    /**
     * 断路器类型 （如：16A 32A 100A 800A）
     */
    private String type;
    /**
     * 11ST 塑壳 M 型配置参数模式 0-本地 1-远程
     */
    private Integer pattern;
    /**
     * 网关 MAC ID 如 MAC：00ABCDEF0123
     */
    private String gateway;
    /**
     * 设备出厂时间，如：20190305（2019/03/05）
     */
    private String factoryDate;
    /**
     * 当前设备设置的漏电电流
     */
    private BigDecimal leakage;
    /**
     * 断路器状态 01H ：断开 ：02H 闭合
     */
    private BreakerStatus status;

    public BreakerInfo(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_33;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {
        BreakerMessage message = (BreakerMessage) requestMessage;
        byte[] data = message.getData();
        this.gateway = ByteUtil.bytesToHex(data, 0, 6);
        this.sn = ByteUtil.bytesToHex(data, 6, 6);
        this.type = ByteUtil.bcdToStr(data, 12, 2);
        this.model = ByteUtil.bcdToStr(data, 14, 1);
        String s = ByteUtil.bcdToStr(data, 15, 1);
        this.status = BreakerStatus.getInstance(s);
        this.hv = ByteUtil.bcdToStr(data, 16, 6);
        this.pv = ByteUtil.bcdToStr(data, 22, 6);
        this.factoryDate = ByteUtil.bcdToStr(data, 28, 4);
        this.leakage = new BigDecimal(ByteUtil.bcdToStr(data, 32, 4));

    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHv() {
        return hv;
    }

    public void setHv(String hv) {
        this.hv = hv;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPattern() {
        return pattern;
    }

    public void setPattern(Integer pattern) {
        this.pattern = pattern;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getFactoryDate() {
        return factoryDate;
    }

    public void setFactoryDate(String factoryDate) {
        this.factoryDate = factoryDate;
    }

    public BigDecimal getLeakage() {
        return leakage;
    }

    public void setLeakage(BigDecimal leakage) {
        this.leakage = leakage;
    }

    public BreakerStatus getStatus() {
        return status;
    }

    public void setStatus(BreakerStatus status) {
        this.status = status;
    }
}
