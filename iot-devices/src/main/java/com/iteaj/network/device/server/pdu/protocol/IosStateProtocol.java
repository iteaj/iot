package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduMessageUtil;
import com.iteaj.network.device.server.pdu.PduTradeType;

/**
 * 获取插口的开关状态: START iostate total='8' io8='254' check='86' END
 * 说明：以 START iostate 开头；total 表示 PDU 的插口数(4,8,16…)；io8 表示各插口的开关二进制状态,
 * 如 254=11111110 （二进制）1 代表开,0 代码关
 */
public class IosStateProtocol extends PduDeviceProtocol{

    //表示各插口的开关二进制状态
    private char[] io8;
    //表示 PDU 的插口数(4,8,16…)
    private Integer total;

    public IosStateProtocol(PduMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected PduMessage doBuildResponseMessage() {
        return null;
    }

    @Override
    protected void resolverRequestMessage(PduMessage requestMessage) {
        String[] content = requestMessage.getContent();
        this.total = PduMessageUtil.getInteger(content, 2);
        this.io8 = PduMessageUtil.getBytes(content, 3);
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.iostate;
    }

    public char[] getIo8() {
        return io8;
    }

    public void setIo8(char[] io8) {
        this.io8 = io8;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
