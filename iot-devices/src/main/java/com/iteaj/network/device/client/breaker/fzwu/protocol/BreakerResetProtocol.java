package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.ProtocolException;
import com.iteaj.network.client.ClientMessage;
import com.iteaj.network.utils.ByteBufUtil;
import com.iteaj.network.device.client.breaker.fzwu.BreakerClientProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;

public class BreakerResetProtocol extends BreakerClientProtocol {

    private ResetType type;

    public BreakerResetProtocol(String gateway, ResetType type) {
        super(gateway, gateway);
        this.type = type;
    }

    @Override
    protected byte[] getBreakerData() {
        if(this.type == null) throw new ProtocolException("未设置设置网关类型");

        return ByteBufUtil.hexToBytes(this.type.value);
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_39;
    }

    @Override
    public void doBuildResponseMessage(ClientMessage message) {

    }

    @Override
    public boolean isRelation() {
        return false;
    }

    public static enum ResetType {
        初始化网关("01"), 清空数据("02");

        public String value;

        ResetType(String value) {
            this.value = value;
        }
    }
}
