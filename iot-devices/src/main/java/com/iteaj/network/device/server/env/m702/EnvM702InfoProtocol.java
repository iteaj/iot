package com.iteaj.network.device.server.env.m702;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerUtils;
import com.iteaj.network.device.elfin.ElfinMessageBody;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.server.protocol.PlatformRequestProtocol;
import com.iteaj.network.utils.ByteUtil;

import java.io.IOException;

/**
 * 读取七合一环境监测设备的环境数据
 */
public class  EnvM702InfoProtocol extends PlatformRequestProtocol {

    /**
     * 二氧化碳值
     */
    private double co2;
    private double tvoc;
    /**
     * 甲醛值
     */
    private double jiaQuan;
    /**
     * pm2.5值
     */
    private double pm25;
    /**
     * pm10值
     */
    private double pm10;
    /**
     * 温度值
     */
    private double wenDu;
    /**
     * 湿度值
     */
    private double shiDu;

    public EnvM702InfoProtocol(String equipCode) {
        super(equipCode);
    }

    @Override
    protected String doGetMessageId() {
        return this.getEquipCode();
    }

    @Override
    protected AbstractMessage doBuildRequestMessage() throws IOException {
        ElfinMessageHeader envElfinHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
        byte[] messageBody = ByteUtil.hexToByte("010300020007");
        ElfinMessageBody envModbusPdu = new ElfinMessageBody(messageBody);

        return new EnvM702Message(envElfinHeader, envModbusPdu);
    }

    @Override
    protected AbstractMessage resolverResponseMessage(AbstractMessage message) {
        int i = BreakerUtils.hexToInt(message.getMessage(), 3, 1) * 256;
        this.co2 = i + BreakerUtils.hexToInt(message.getMessage(), 4, 1);

        i = BreakerUtils.hexToInt(message.getMessage(), 5, 1) * 256;
        this.jiaQuan = i + BreakerUtils.hexToInt(message.getMessage(), 6, 1);

        i = BreakerUtils.hexToInt(message.getMessage(), 7, 1) * 256;
        this.tvoc = i + BreakerUtils.hexToInt(message.getMessage(), 8, 1);

        i = BreakerUtils.hexToInt(message.getMessage(), 9, 1) * 256;
        this.pm25 = i + BreakerUtils.hexToInt(message.getMessage(), 10, 1);

        i = BreakerUtils.hexToInt(message.getMessage(), 11, 1) * 256;
        this.pm10 = i + BreakerUtils.hexToInt(message.getMessage(), 12, 1);

        i = BreakerUtils.hexToInt(message.getMessage(), 13, 1) * 256;
        this.wenDu = (i + BreakerUtils.hexToInt(message.getMessage(), 14, 1)) / 10;

        i = BreakerUtils.hexToInt(message.getMessage(), 15, 1) * 256;
        this.shiDu = (i + BreakerUtils.hexToInt(message.getMessage(), 16, 1)) / 10;
        return message;
    }

    @Override
    public ElfinType protocolType() {
        return ElfinType.Env_Elfin_M702;
    }

    public double getCo2() {
        return co2;
    }

    public void setCo2(double co2) {
        this.co2 = co2;
    }

    public double getTvoc() {
        return tvoc;
    }

    public void setTvoc(double tvoc) {
        this.tvoc = tvoc;
    }

    public double getJiaQuan() {
        return jiaQuan;
    }

    public void setJiaQuan(double jiaQuan) {
        this.jiaQuan = jiaQuan;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public double getWenDu() {
        return wenDu;
    }

    public void setWenDu(double wenDu) {
        this.wenDu = wenDu;
    }

    public double getShiDu() {
        return shiDu;
    }

    public void setShiDu(double shiDu) {
        this.shiDu = shiDu;
    }
}
