package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;

/**
 * 设备上报网关数据
 */
public class BreakerGatewayInfo extends BreakerDeviceRequestProtocol {

    private String pv; // 6字节 程序版本
    private String sn; // 6字节 硬件版本号 如 version：000012345678
    private String macId; // 6字节 网关 MAC ID 如 MAC：00ABCDEF0123
    /**
     * 网关型号 1字节
     * 1:WIFI 网口网关 + 485
     * 2:2G 网关 + 485
     * 3:4G 网关 + 485
     * 4:NB 网关 + 485
     */
    private byte dataModel;

    public BreakerGatewayInfo(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_32;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {
        BreakerMessage breakerMessage = (BreakerMessage) requestMessage;
        byte[] data = breakerMessage.getData();

        this.macId = ByteUtil.bytesToHex(data, 0, 6);
        String s = ByteUtil.bcdToStr(data, 6, 12);
        this.sn = s.substring(0, 12);
        this.pv = s.substring(12, 24);
        this.dataModel = data[18];
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public byte getDataModel() {
        return dataModel;
    }

    public void setDataModel(byte dataModel) {
        this.dataModel = dataModel;
    }
}
