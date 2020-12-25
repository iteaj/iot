package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduMessageUtil;
import com.iteaj.network.device.server.pdu.PduTradeType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PvcInfoProtocol extends PduDeviceProtocol{

    // 功率
    private double p;
    // 扩大 100 倍的电压
    private double v;
    // 扩大 100 倍的电流
    private double c;
    // 电量
    private double e;

    // C0-c16：扩大 100 倍电流
    // 电流列表
    List<Double> cList = new ArrayList<>();
    // 扩大 10 的温度值
    private double t;
    private double f; // 华氏
    private double h; // 湿度

    private String tflag;

    public PvcInfoProtocol(PduMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.PVC_Info;
    }

    @Override
    protected void resolverRequestMessage(PduMessage requestMessage) {
        String[] content = requestMessage.getContent();
        this.p = PduMessageUtil.getInteger(content, 3);
        this.v = PduMessageUtil.getInteger(content, 4) / 100.0;
        this.c = PduMessageUtil.getInteger(content, 5) / 100.0;

        cList.add(PduMessageUtil.getInteger(content, 7) / 100.0);
        cList.add(PduMessageUtil.getInteger(content, 8) / 100.0);
        cList.add(PduMessageUtil.getInteger(content, 9) / 100.0);
        cList.add(PduMessageUtil.getInteger(content, 10) / 100.0);
        int index = 10;

        // 说明至少是8插口
        if(PduMessageUtil.startWith(content, index + 1, "c4")) {
            index = getIndex(content, index);
        }

        // 说明是16插口
        if(PduMessageUtil.startWith(content, index + 1, "c8")) {
            index = getIndex(content, index);
            index = getIndex(content, index);
        }

        this.e = PduMessageUtil.getBigDecimal(content, ++index).doubleValue();

        this.tflag = PduMessageUtil.getString(content, ++index);
        if(this.tflag.equals("1")) { // 有温度传感器
            this.t = PduMessageUtil.getInteger(content, ++index);
            this.f = PduMessageUtil.getInteger(content, ++index);
            this.h = PduMessageUtil.getInteger(content, ++index);
        }
    }

    private int getIndex(String[] content, int index) {
        cList.add(PduMessageUtil.getInteger(content, ++index) / 100.0);
        cList.add(PduMessageUtil.getInteger(content, ++index) / 100.0);
        cList.add(PduMessageUtil.getInteger(content, ++index) / 100.0);
        cList.add(PduMessageUtil.getInteger(content, ++index) / 100.0);
        return index;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public List<Double> getcList() {
        return cList;
    }

    public void setcList(List<Double> cList) {
        this.cList = cList;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }
}
