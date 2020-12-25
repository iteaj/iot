package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;
import com.iteaj.network.device.client.breaker.fzwu.BreakerUtils;
import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.utils.ByteUtil;

/**
 * 单相断路器主动发送当前开关状态协议
 */
public class BreakerBrokerStatusProtocol extends BreakerDeviceRequestProtocol {

    private BreakerStatus status;

    /**
     * 01H : 机械动作回复 02H : 获取状态回复
     */
    private String type;
    private String errMsg;
    private Integer errCode;

    public BreakerBrokerStatusProtocol(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_37;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {
        BreakerMessage breakerMessage = (BreakerMessage) requestMessage;
        byte[] data = breakerMessage.getData();

        String execStatus = ByteUtil.bytesToHex(data, 0, 1);
        this.status = BreakerStatus.getInstance(execStatus);
        this.errCode = BreakerUtils.handleErrCode(ByteUtil.subBytes(data, 1, 5));
        this.type = ByteUtil.bytesToHex(data, 5, 1);
    }

    public BreakerStatus getStatus() {
        return status;
    }

    public void setStatus(BreakerStatus status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }
}
