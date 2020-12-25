package com.iteaj.network.device.server.ths;

import com.iteaj.network.device.elfin.ElfinMessageBody;
import com.iteaj.network.device.elfin.ElfinMessageHeader;
import com.iteaj.network.device.elfin.ElfinType;
import com.iteaj.network.utils.ByteUtil;

import java.io.IOException;

/**
 * 温湿度获取, 携带的传感器开关量获取 协议
 */
public class THSInfoProtocol extends ThsPlatformProtocol {

    /**
     * 请求传感器编辑
     */
    private boolean flag;
    private Double wenDu;
    private Double shiDu;
    /**
     * 开关量状态
     * 1. 正常
     * 2. 异常
     */
    private int status;
    /**
     * 携带的传感器类型
     */
    private ThsSensor sensor;

    public THSInfoProtocol(String equipCode) {
        super(equipCode);
    }

    public THSInfoProtocol(String equipCode, ThsSensor sensor) {
        super(equipCode);
        this.sensor = sensor;
    }

    @Override
    protected String doGetMessageId() {
        return getEquipCode();
    }

    @Override
    protected ThsElfinMessage doBuildRequestMessage() throws IOException {
        if(!flag) { // 第一次请求温湿度
            ElfinMessageHeader envElfinHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
            byte[] messageBody = ByteUtil.hexToByte("EA01016490");
            ElfinMessageBody envModbusPdu = new ElfinMessageBody(messageBody);

            return new ThsElfinMessage(envElfinHeader, envModbusPdu);
        } else if(sensor != null) { // 如果有传感器类型, 在请求传感器
            ElfinMessageHeader envElfinHeader = new ElfinMessageHeader(getEquipCode(), protocolType());
            byte[] messageBody = ByteUtil.hexToByte("EA010265D0");
            ElfinMessageBody envModbusPdu = new ElfinMessageBody(messageBody);

            return new ThsElfinMessage(envElfinHeader, envModbusPdu);
        }

        return null;
    }

    @Override
    protected ThsElfinMessage resolverResponseMessage(ThsElfinMessage message) {
        byte[] messageMessage = message.getMessage();
        String hex = ByteUtil.bytesToHex(messageMessage);
        if(!this.flag) {
            String shiDuHex = hex.substring(4, 8);
            String tempHex = hex.substring(8, 12);
            this.wenDu = Double.valueOf(Integer.valueOf(tempHex, 16)/10);
            this.shiDu = Double.valueOf(Integer.valueOf(shiDuHex, 16)/10);
        } else if(this.sensor != null){
            String smokeStatus = hex.substring(4, 6);

            if("00".equals(smokeStatus)) { // 状态正常
                this.status = 1;
            } else if("FF".equalsIgnoreCase(smokeStatus)) { // 异常
                this.status = 2;
            }
        }

        return message;
    }

    @Override
    public ElfinType protocolType() {
        return ElfinType.Env_Elfin_THS;
    }

    public Double getWenDu() {
        return wenDu;
    }

    public Double getShiDu() {
        return shiDu;
    }

    public int getStatus() {
        return status;
    }

    public ThsSensor getSensor() {
        return sensor;
    }

    public boolean isFlag() {
        return flag;
    }

    public THSInfoProtocol setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }
}
