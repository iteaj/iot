package com.iteaj.network.device.client.breaker.fzwu.protocol;

import com.iteaj.network.client.ClientMessage;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.utils.ByteUtil;
import com.iteaj.network.device.client.breaker.fzwu.BreakerMessage;
import com.iteaj.network.device.client.breaker.fzwu.BreakerType;
import com.iteaj.network.device.client.breaker.fzwu.BreakerUtils;

/**
 * 0 过压异常（总、A、B、C）
 * 1 欠压异常（总、A、B、C）
 * 2 过流异常（总、A、B、C、N）
 * 3 开关状态异常，提醒客户更换断路器（总）
 * 4 漏电异常（总）
 * 5 短路异常（总、A、B、C）
 * 6 温度异常（总、A、B、C、N）
 * 7 线路电弧异常（总、A、B、C）
 * 8 过载异常（总、A、B、C）
 * 9 最小功率异常（总、A、B、C）
 * 10 漏电功能已坏，提醒客户更换断路器（总）
 * 11 进入维修模式(手动模式)（总）
 * 12 断零异常（总、N）
 * 13 三相不平衡异常（总）
 * 14 缺相异常（总、A、B、C）
 * 15~31 保留
 */
public class Breaker4PStatusProtocol extends BreakerStatusProtocol {


    private String aMsg; // A相消息
    private String bMsg; // B相消息
    private String cMsg; // C相消息

    private String errStatus;

    /**
     * @param sn      断路器编号
     * @param gateway 网关编号
     * @param status  操作状态
     */
    public Breaker4PStatusProtocol(String sn, String gateway, BreakerStatus status) {
        super(sn, gateway, status);
    }

    @Override
    public void doBuildResponseMessage(ClientMessage message) {
        BreakerMessage breakerMessage = (BreakerMessage) message;
        byte[] data = breakerMessage.getData();

        this.errStatus = ByteUtil.bytesToHex(data, 0, 1);
        String status = ByteUtil.bytesToHex(data, 1, 1);
        setStatus(BreakerStatus.getInstance(status));
        this.setErrCode(BreakerUtils.handleErrCode(ByteUtil.subBytes(data, 2, 6)));
    }

    @Override
    public boolean isOk() {
        return getExecStatus() == ExecStatus.成功 && this.errStatus.equals("01");
    }

    @Override
    public BreakerType protocolType() {
        return BreakerType.BH_4E;
    }

    @Override
    public String getErrMsg() {
        if(getExecStatus() != ExecStatus.成功) {
            switch (getErrCode()) {
                case 0: return "过压异常（总、A、B、C）";
                case 1: return "欠压异常（总、A、B、C）";
                case 2: return "过流异常（总、A、B、C、N）";
                case 3: return "开关状态异常，提醒客户更换断路器（总）";
                case 4: return "漏电异常（总）";
                case 5: return "短路异常（总、A、B、C）";
                case 6: return "温度异常（总、A、B、C、N）";
                case 7: return "线路电弧异常（总、A、B、C）";
                case 8: return "过载异常（总、A、B、C）";
                case 9: return "最小功率异常（总、A、B、C）";
                case 10: return "漏电功能已坏，提醒客户更换断路器（总）";
                case 11: return "进入维修模式(手动模式)（总）";
                case 12: return "断零异常（总、N）";
                case 13: return "三相不平衡异常（总）";
                case 14: return "缺相异常（总、A、B、C）";
                default: return "";
            }
        } else {
            return getExecStatus().desc;
        }
    }

    public String getaMsg() {
        return aMsg;
    }

    public void setaMsg(String aMsg) {
        this.aMsg = aMsg;
    }

    public String getbMsg() {
        return bMsg;
    }

    public void setbMsg(String bMsg) {
        this.bMsg = bMsg;
    }

    public String getcMsg() {
        return cMsg;
    }

    public void setcMsg(String cMsg) {
        this.cMsg = cMsg;
    }
}
