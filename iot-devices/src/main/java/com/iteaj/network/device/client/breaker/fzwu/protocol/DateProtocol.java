package com.iteaj.network.device.client.breaker.fzwu.protocol;

import cn.hutool.core.date.DateUtil;
import com.iteaj.network.message.MqttClientMessage;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.breaker.fzwu.BreakerDeviceRequestProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;

import java.util.Date;

public class DateProtocol extends BreakerDeviceRequestProtocol {

    public DateProtocol(BreakerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void setResponseData(BreakerMessage responseMessage) {
        String yyMMddHHmmss = DateUtil.format(new Date(), "yyMMddHHmmss");
        responseMessage.setData(ByteUtil.str2Bcd(yyMMddHHmmss));
    }

    /**
     * 时间协议不存在暂用时间的操作, 所以直接同步执行
     * @return
     */
    @Override
    protected boolean isAsyncExec() {
        return false;
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_31;
    }

    @Override
    protected void doBuildRequestMessage(MqttClientMessage requestMessage) {

    }
}
