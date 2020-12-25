package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;
import com.iteaj.network.device.client.breaker.fzwu.BreakerUtils;

import java.math.BigDecimal;

/**
 * 设备上报断路器预警信息
 * @see BreakerType#BH_36
 */
public class BreakerErrorWarning extends BreakerDeviceRequestProtocol {

    private String coincidence; // 1 hex重合次数
    private Integer errCode; // 4 hex 错误码
    private BigDecimal voltage; // 2 HEX data 电压 单位 V
    private BigDecimal current; // 2 HEX data 电流 单位 A
    private BigDecimal power; // 4 HEX Data 功率 单位 W
    private BigDecimal temper; //1 HEX Data 温度 单位度,补码
    private BigDecimal leakage; // 2 HEX Data 漏电值 单位 mA
    private String type; // 1 HEX Data 错误类型(01-预警 02-报警)

    public BreakerErrorWarning(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_36;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {
        BreakerMessage breakerMessage = (BreakerMessage) requestMessage;
        byte[] data = breakerMessage.getData();
        this.coincidence = ByteUtil.bytesToHex(data, 0, 1);
        this.errCode = BreakerUtils.handleErrCode(ByteUtil.subBytes(data, 1, 5));
        this.voltage = new BigDecimal(ByteUtil.bytesToHex(data, 5, 2));
        this.current = new BigDecimal(ByteUtil.bytesToHex(data, 7, 2));
        this.power = new BigDecimal(ByteUtil.bytesToHex(data, 9, 4));
        this.temper = new BigDecimal(ByteUtil.bytesToHex(data, 13, 1));
        this.leakage = new BigDecimal(ByteUtil.bytesToHex(data, 14, 2));
        this.type = ByteUtil.bytesToHex(data, 16, 1);
    }

    /**
     * 0 过压异常
     * 1 欠压异常
     * 2 过流异常
     * 3 开关状态异常，提醒客户更换断路器
     * 4 漏电异常
     * 5 短路异常
     * 6 断路器温度过高异常
     * 7 线路打火异常
     * 8 最大功率异常
     * 9 最小功率异常
     * 10 漏电功能已坏异常，提醒客户更换断路器
     * 11 进入维修模式(手动掰下断路器)
     * 12 断零异常（不存在）
     * 13 三相不平衡异常（不存在）
     * 14 缺相异常（不存在）
     * 15 锁死异常
     * 16 过压预警
     * 17 欠压预警
     * 18 过流预警
     * 19 温度预警
     * 20~31 保留
     * @return
     */
    public String getErrMsg() {
        switch (this.errCode) {
            case 0: return "过压异常";
            case 1: return "欠压异常";
            case 2: return "过流异常";
            case 3: return "开关状态异常，提醒客户更换断路器";
            case 4: return "漏电异常";
            case 5: return "短路异常";
            case 6: return "断路器温度过高异常";
            case 7: return "线路打火异常";
            case 8: return "最大功率异常";
            case 9: return "最小功率异常";
            case 10: return "漏电功能已坏异常，提醒客户更换断路器";
            case 11: return "进入维修模式(手动掰下断路器)";
            case 12: return "断零异常（不存在）";
            case 13: return "三相不平衡异常（不存在）";
            case 14: return "缺相异常（不存在）";
            case 15: return "锁死异常";
            case 16: return "过压预警";
            case 17: return "欠压预警";
            case 18: return "过流预警";
            case 19: return "温度预警";
        }
        return "";
    }
    public String getChildType(int type) {
        return BreakerUtils.getChildType(type);
    }
    public String getCoincidence() {
        return coincidence;
    }

    public void setCoincidence(String coincidence) {
        this.coincidence = coincidence;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public BigDecimal getVoltage() {
        return voltage;
    }

    public void setVoltage(BigDecimal voltage) {
        this.voltage = voltage;
    }

    public BigDecimal getCurrent() {
        return current;
    }

    public void setCurrent(BigDecimal current) {
        this.current = current;
    }

    public BigDecimal getPower() {
        return power;
    }

    public void setPower(BigDecimal power) {
        this.power = power;
    }

    public BigDecimal getTemper() {
        return temper;
    }

    public void setTemper(BigDecimal temper) {
        this.temper = temper;
    }

    public BigDecimal getLeakage() {
        return leakage;
    }

    public void setLeakage(BigDecimal leakage) {
        this.leakage = leakage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
