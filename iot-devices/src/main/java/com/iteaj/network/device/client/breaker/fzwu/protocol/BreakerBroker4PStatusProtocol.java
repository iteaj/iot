package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;
import com.iteaj.network.device.client.breaker.fzwu.BreakerUtils;
import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.utils.ByteUtil;

/**
 * 3相断路器主动发送当前开关状态协议
 */
public class BreakerBroker4PStatusProtocol extends BreakerDeviceRequestProtocol {

    private BreakerStatus status;

    private String errStatus;
    /**
     * 01H : 机械动作回复 02H : 获取状态回复
     */
    private String type;
    private String errMsg;
    private Integer errCode;

    public BreakerBroker4PStatusProtocol(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_4E;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {
        BreakerMessage breakerMessage = (BreakerMessage) requestMessage;
        byte[] data = breakerMessage.getData();

        this.errStatus = ByteUtil.bytesToHex(data, 0, 1);
        String status = ByteUtil.bytesToHex(data, 1, 1);
        setStatus(BreakerStatus.getInstance(status));
        this.setErrCode(BreakerUtils.handleErrCode(ByteUtil.subBytes(data, 2, 6)));
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
