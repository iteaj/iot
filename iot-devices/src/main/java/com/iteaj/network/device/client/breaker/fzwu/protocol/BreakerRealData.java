package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;
import com.iteaj.network.device.client.breaker.fzwu.BreakerUtils;

/**
 * 单相断路器上送的数据
 */
public class BreakerRealData extends BreakerDeviceRequestProtocol {

    private BreakerStatus status; //ERROR 1 HEX data 0X01：断开 0X02：闭合 03：离线
    private Double va; //DATA1-V 2 HEX data 电压值 85~460VAC
    private Double ia; //DATA2-A 2 HEX data 电流值 0 到 80A
    private int ip; //DATA3-AF 1 HEX data 电流小数值 0 到 99A
    private Double totalAp; //DATA4-P 4 HEX data 有功功率值 0 到 3000W
    private int aapp; //DATA5-P 1 HEX data 有功功率小数点 0-99
    private Double totalRp; //DATA4-P 4 HEX data 无功功率值 0 到 3000W
    private int arpp; //DATA5-P 1 HEX data 无功功率小数点 0-99
    private Double rfc; //DATA5-P 1 HEX data 功率因素 百分比
    private Double rc; //DATA6-mA 2 HEX data 漏电电流

    private Double power; // 电量值
    private Integer times; // 开关次数
    private Integer flagTime; //多长时间上报一次
    public BreakerRealData(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {
        BreakerMessage breakerMessage = (BreakerMessage) requestMessage;
        byte[] src = breakerMessage.getData();

        this.status = BreakerStatus.getInstance(ByteUtil.bytesToHex(src, 0, 1));
        this.va = Double.valueOf(BreakerUtils.hexToInt(src, 1, 2));
        this.ip = BreakerUtils.hexToInt(src, 5, 1);
        this.ia = Double.valueOf(BreakerUtils.hexToInt(src, 3, 2)+"."+this.ip);
        this.aapp = BreakerUtils.hexToInt(src, 10, 1);
        this.totalAp = Double.valueOf(BreakerUtils.hexToInt(src, 6, 4) + "." + this.aapp);

        this.arpp = BreakerUtils.hexToInt(src, 15, 1);
        this.totalRp = Double.valueOf(BreakerUtils.hexToInt(src, 11, 4) + "." + this.arpp);

        this.rfc = Double.valueOf(BreakerUtils.hexToInt(src, 16, 1));
        this.rc = Double.valueOf(BreakerUtils.hexToInt(src, 17, 2));

        int power = BreakerUtils.hexToInt(src, 19, 4); // 电量整数值
        int pip = BreakerUtils.hexToInt(src, 23, 1); // 电量小数值
        this.power = Double.valueOf(power+"."+pip);
        this.times = BreakerUtils.hexToInt(src, 24, 4);
        this.flagTime = BreakerUtils.hexToInt(src, 28, 1);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_35;
    }

    public BreakerStatus getStatus() {
        return status;
    }

    public void setStatus(BreakerStatus status) {
        this.status = status;
    }

    public Double getVa() {
        return va;
    }

    public void setVa(Double va) {
        this.va = va;
    }

    public Double getIa() {
        return ia;
    }

    public void setIa(Double ia) {
        this.ia = ia;
    }

    public int getIp() {
        return ip;
    }

    public void setIp(int ip) {
        this.ip = ip;
    }

    public Double getTotalAp() {
        return totalAp;
    }

    public void setTotalAp(Double totalAp) {
        this.totalAp = totalAp;
    }

    public int getAapp() {
        return aapp;
    }

    public void setAapp(int aapp) {
        this.aapp = aapp;
    }

    public Double getTotalRp() {
        return totalRp;
    }

    public void setTotalRp(Double totalRp) {
        this.totalRp = totalRp;
    }

    public int getArpp() {
        return arpp;
    }

    public void setArpp(int arpp) {
        this.arpp = arpp;
    }

    public Double getRfc() {
        return rfc;
    }

    public void setRfc(Double rfc) {
        this.rfc = rfc;
    }

    public Double getRc() {
        return rc;
    }

    public void setRc(Double rc) {
        this.rc = rc;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getFlagTime() {
        return flagTime;
    }

    public void setFlagTime(Integer flagTime) {
        this.flagTime = flagTime;
    }
}
