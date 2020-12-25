package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.ProtocolException;
import com.iteaj.network.client.ClientMessage;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.utils.ByteBufUtil;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.breaker.fzwu.BreakerClientProtocol;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;
import com.iteaj.network.device.client.breaker.fzwu.BreakerUtils;

/**
 * 0 过压异常
 * 1 欠压异常
 * 2 过流异常
 * 3 开关状态异常，提醒客户更换断路器
 * 4 漏电异常
 * 5 短路异常
 * 6 断路器温度过高异常
 * 7 线路打火异常
 * 8 最大功率异常
 * 9 最小功率异常
 * 10 漏电功能已坏异常，提醒客户更换断路器
 * 11 进入维修模式(手动掰下断路器)
 * 12~31 保留
 *
 * 单相断路器状态切换协议
 */
public class BreakerStatusProtocol extends BreakerClientProtocol {

    private BreakerStatus status;

    /**
     * 01H : 机械动作回复 02H : 获取状态回复
     */
    private String type;
    private String errMsg;
    private Integer errCode;

    /**
     * 操作次网关下面的所有断路器
     * @param gateway
     * @param status
     */
    public BreakerStatusProtocol(String gateway, BreakerStatus status) {
        this("000000000000", gateway, status);
        this.status = status;
        if(status != BreakerStatus.ALL_Close || status != BreakerStatus.All_Open) {
            throw new ProtocolException("只能使用全开 全关操作");
        }
    }

    /**
     *
     * @param sn 断路器编号
     * @param gateway 网关编号
     * @param status 操作状态
     */
    public BreakerStatusProtocol(String sn, String gateway, BreakerStatus status) {
        super(sn, gateway);
        this.status = status;
    }

    /**
     * 01H ：断开 ：02H 闭合 03H:失联 04H：失败
     * @return
     */
    public boolean isOk() {
        return getExecStatus() == ExecStatus.成功;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getErrMsg() {
        if(null == errCode) return getExecStatus().desc;

        if(getExecStatus() != ExecStatus.成功) {
            switch (errCode) {
                case 0: return "过压异常";
                case 1: return "欠压异常";
                case 2: return "过流异常";
                case 3: return "开关状态异常，提醒客户更换断路器";
                case 4: return "漏电异常";
                case 5: return "短路异常";
                case 6: return "断路器温度过高异常";
                case 7: return "线路打火异常";
                case 8: return "最大功率异常";
                case 9: return "最小功率异常";
                case 10: return "漏电功能已坏异常，提醒客户更换断路器";
                case 11: return "进入维修模式(手动掰下断路器)";
                default: return "";
            }
        } else {
            this.errMsg = getExecStatus().desc;
        }
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

    public BreakerStatus getStatus() {
        return status;
    }

    public void setStatus(BreakerStatus status) {
        this.status = status;
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_37;
    }

    @Override
    protected byte[] getBreakerData() {
        return ByteBufUtil.hexToBytes(status.hex);
    }

    @Override
    public void doBuildResponseMessage(ClientMessage message) {
        BreakerMessage breakerMessage = (BreakerMessage) message;
        byte[] data = breakerMessage.getData();

        String execStatus = ByteUtil.bytesToHex(data, 0, 1);
        this.status = BreakerStatus.getInstance(execStatus);
        this.errCode = BreakerUtils.handleErrCode(ByteUtil.subBytes(data, 1, 5));
        this.type = ByteUtil.bytesToHex(data, 5, 1);
    }
}
