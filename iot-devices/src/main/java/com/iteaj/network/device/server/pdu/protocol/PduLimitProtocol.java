package com.iteaj.network.device.server.pdu.protocol;

import com.iteaj.network.device.server.pdu.PduMessage;
import com.iteaj.network.device.server.pdu.PduMessageUtil;
import com.iteaj.network.device.server.pdu.PduTradeType;

import java.math.BigDecimal;

/**
 * 此协议在触发上下限值时会被触发
 */
public class PduLimitProtocol extends PduDeviceProtocol {

    /**
     * P 功率
     * V 电压
     * A 电流
     */
    private String type;
    private BigDecimal v; // 电压值
    private BigDecimal p; // 功率值
    private BigDecimal a; // 电流值

    private static BigDecimal oh = new BigDecimal(100);

    public PduLimitProtocol(PduMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    public PduTradeType protocolType() {
        return PduTradeType.limit;
    }

    @Override
    protected void resolverRequestMessage(PduMessage requestMessage) {
        String[] content = requestMessage.getContent();
        this.type = content[2];
        this.p = PduMessageUtil.getBigDecimal(content, 3);
        this.a = PduMessageUtil.getBigDecimal(content, 4).divide(oh);
        this.v = PduMessageUtil.getBigDecimal(content, 5).divide(oh);
    }

    public String getType() {
        return type;
    }

    public BigDecimal getV() {
        return v;
    }

    public BigDecimal getP() {
        return p;
    }

    public BigDecimal getA() {
        return a;
    }
}
