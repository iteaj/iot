package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;
import com.iteaj.network.device.client.breaker.fzwu.BreakerUtils;
import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.utils.ByteUtil;

/**
 * 设备上报三相重合闸实时数据
 * @see BreakerType#BH_48
 */
public class BreakerThreeRealData extends BreakerDeviceRequestProtocol {

    /**
     * 接收的数据
     */
    private String totalStatus; //总状态 1 HEX data 01H ：断开 ：02H 闭合 03H:离线
    private double totalV; //总电压 2 HEX data 总电压，精确度 0.1V
    private double totalI; //总电流 2 HEX data 总电流，精确度 0.01A
    private double totalQ; //总电量 4 HEX data 总电量，精确度 0.01KW.H
    private double rc; //总漏电 2 HEX data 总漏电，精确度 1mA
    private double totalAp; //总有功功率 3 HEX data 有功总功率，精确度 1W
    private double totalRp; //总无功功率 3 HEX data 无功总功率，精确度 1W
    private double rfc; //功率因素 1 HEX data 功率因素 百分比
    private double va; //A 相电压 2 HEX data A 相电压，精确度 0.1V
    private double ia; //A 相电流 2 HEX data A 相电流，精确度 0.01A
    private double aap; //A 有功功率 3 HEX data A 有功功率，精确度 1W
    private double arp; //A 无功功率 3 HEX data A 无功功率，精确度 1W
    private double arfc; //A 功率因素 1 HEX data A 功率因素 百分比
    private double act; //A 相温度 1 HEX data A 相温度，补码格式，-127~128 温度
    private double vb; //B 相电压 2 HEX data B 相电压，精确度 0.1V
    private double ib; //B 相电流 2 HEX data B 相电流，精确度 0.01A
    private double bap; //B 有功功率 3 HEX data B 功率，精确度 1W
    private double brp; //B 无功功率 3 HEX data B 功率，精确度 1W
    private double brfc; //B 功率因素 1 HEX data B 功率因素 百分比
    private double bct; //B 相温度 1 HEX data B 相温度，补码格式，-127~128 温度
    private double vc; //C 相电压 2 HEX data C 相电压，精确度 0.1V
    private double ic; //C 相电流 2 HEX data C 相电流，精确度 0.01A
    private double cap; //C 有功功率 3 HEX data C 有功功率，精确度 1W
    private double crp; //C 无功功率 3 HEX data C 无功功率，精确度 1W
    private double crfc; //C 功率因素 1 HEX data C 功率因素 百分比
    private double cct; //C 相温度 1 HEX data C 相温度，补码格式，-127~128 温度
    private double ni; //N 线电流 2 HEX data N 线电流，精确度 0.01A
    private double nct; //N 线温度 1 HEX data N 线温度，补码格式，-127~128 温度

    private int times; // 开关次数

    public BreakerThreeRealData(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {
        BreakerMessage breakerMessage = (BreakerMessage) requestMessage;
        byte[] data = breakerMessage.getData();

        this.totalStatus = ByteUtil.bytesToHex(data, 0, 1);
        this.totalV = BreakerUtils.hexToInt(data, 1, 2) * 0.1;
        this.totalI = BreakerUtils.hexToInt(data, 3, 2) * 0.01;
        this.totalQ = BreakerUtils.hexToInt(data, 5, 4) * 0.01;
        this.rc = BreakerUtils.hexToInt(data, 9, 2);
        this.totalAp = BreakerUtils.hexToInt(data, 11, 3);
        this.totalRp = BreakerUtils.hexToInt(data, 14, 3);

        this.rfc = BreakerUtils.hexToInt(data, 17, 1);
        this.va = BreakerUtils.hexToInt(data, 18, 2) * 0.1;
        this.ia = BreakerUtils.hexToInt(data, 20, 2) * 0.01;

        this.aap = BreakerUtils.hexToInt(data, 22, 3);
        this.arp = BreakerUtils.hexToInt(data, 25, 3);

        this.arfc = BreakerUtils.hexToInt(data, 28, 1);
        this.act =  ByteUtil.subBytes(data, 29, 30)[0]; //BreakerUtils.hexToInt(data, 29, 1);

        this.vb = BreakerUtils.hexToInt(data, 30, 2) * 0.1;
        this.ib = BreakerUtils.hexToInt(data,32, 2) * 0.01;
        this.bap = BreakerUtils.hexToInt(data, 34, 3);
        this.brp = BreakerUtils.hexToInt(data, 37, 3);
        this.brfc = BreakerUtils.hexToInt(data, 40, 1);
        this.bct = ByteUtil.subBytes(data, 41, 42)[0];

        this.vc = BreakerUtils.hexToInt(data, 42, 2) * 0.1;
        this.ic = BreakerUtils.hexToInt(data,44, 2) * 0.01;
        this.cap = BreakerUtils.hexToInt(data, 46, 3);
        this.crp = BreakerUtils.hexToInt(data, 49, 3);
        this.crfc = BreakerUtils.hexToInt(data, 52, 1);
        this.cct = ByteUtil.subBytes(data, 53, 54)[0];

        this.ni = BreakerUtils.hexToInt(data, 54, 2) * 0.01;
        this.nct = BreakerUtils.hexToInt(data, 56, 1);
        this.times = BreakerUtils.hexToInt(data, 57, 4);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_48;
    }

    public String getTotalStatus() {
        return totalStatus;
    }

    public void setTotalStatus(String totalStatus) {
        this.totalStatus = totalStatus;
    }

    public double getTotalV() {
        return totalV;
    }

    public void setTotalV(double totalV) {
        this.totalV = totalV;
    }

    public double getTotalI() {
        return totalI;
    }

    public void setTotalI(double totalI) {
        this.totalI = totalI;
    }

    public double getTotalQ() {
        return totalQ;
    }

    public void setTotalQ(double totalQ) {
        this.totalQ = totalQ;
    }

    public double getRc() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public double getTotalAp() {
        return totalAp;
    }

    public void setTotalAp(double totalAp) {
        this.totalAp = totalAp;
    }

    public double getTotalRp() {
        return totalRp;
    }

    public void setTotalRp(double totalRp) {
        this.totalRp = totalRp;
    }

    public double getRfc() {
        return rfc;
    }

    public void setRfc(double rfc) {
        this.rfc = rfc;
    }

    public double getVa() {
        return va;
    }

    public void setVa(double va) {
        this.va = va;
    }

    public double getIa() {
        return ia;
    }

    public void setIa(double ia) {
        this.ia = ia;
    }

    public double getAap() {
        return aap;
    }

    public void setAap(double aap) {
        this.aap = aap;
    }

    public double getArp() {
        return arp;
    }

    public void setArp(double arp) {
        this.arp = arp;
    }

    public double getArfc() {
        return arfc;
    }

    public void setArfc(double arfc) {
        this.arfc = arfc;
    }

    public double getVb() {
        return vb;
    }

    public void setVb(double vb) {
        this.vb = vb;
    }

    public double getIb() {
        return ib;
    }

    public void setIb(double ib) {
        this.ib = ib;
    }

    public double getBap() {
        return bap;
    }

    public void setBap(double bap) {
        this.bap = bap;
    }

    public double getBrp() {
        return brp;
    }

    public void setBrp(double brp) {
        this.brp = brp;
    }

    public double getBrfc() {
        return brfc;
    }

    public void setBrfc(double brfc) {
        this.brfc = brfc;
    }

    public double getVc() {
        return vc;
    }

    public void setVc(double vc) {
        this.vc = vc;
    }

    public double getIc() {
        return ic;
    }

    public void setIc(double ic) {
        this.ic = ic;
    }

    public double getCap() {
        return cap;
    }

    public void setCap(double cap) {
        this.cap = cap;
    }

    public double getCrp() {
        return crp;
    }

    public void setCrp(double crp) {
        this.crp = crp;
    }

    public double getCrfc() {
        return crfc;
    }

    public void setCrfc(double crfc) {
        this.crfc = crfc;
    }

    public double getNi() {
        return ni;
    }

    public void setNi(double ni) {
        this.ni = ni;
    }

    public double getNct() {
        return nct;
    }

    public void setNct(int nct) {
        this.nct = nct;
    }

    public double getAct() {
        return act;
    }

    public void setAct(double act) {
        this.act = act;
    }

    public double getBct() {
        return bct;
    }

    public void setBct(double bct) {
        this.bct = bct;
    }

    public double getCct() {
        return cct;
    }

    public void setCct(double cct) {
        this.cct = cct;
    }

    public int getTimes() {
        return times;
    }
}
