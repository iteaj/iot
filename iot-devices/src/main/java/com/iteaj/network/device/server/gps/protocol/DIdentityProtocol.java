package com.iteaj.network.device.server.gps.protocol;

import com.iteaj.network.device.server.gps.GpsCommonRespResult;
import com.iteaj.network.device.server.gps.GpsMessage;
import com.iteaj.network.device.server.gps.GpsProtocolType;
import com.iteaj.network.device.server.gps.consts.DIdentityStatus;
import com.iteaj.network.device.server.gps.consts.IcResult;
import com.iteaj.network.utils.ByteUtil;

/**
 * create time: 2021/1/22
 *  驾驶员身份上报协议
 * @author iteaj
 * @since 1.0
 */
public class DIdentityProtocol extends GpsDeviceRequestProtocol{

    /**
     * 插拔卡类型
     */
    private DIdentityStatus status;

    /**
     * 插拔卡操作的时间
     */
    private String statusTime;

    /**
     * IC卡读取结果
     */
    private IcResult icResult;

    /**
     * 驾驶员姓名
     */
    private String name;

    /**
     * 从业资格证编码
     */
    private String sacNo;

    /**
     * 发证机构名称
     */
    private String tcbName;

    /**
     * 响应给设备的结果
     */
    private GpsCommonRespResult result = GpsCommonRespResult.成功;

    public DIdentityProtocol(GpsMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected GpsMessage doBuildResponseMessage() {
        return GpsMessage.buildPlatformCommonRespMessageByRequest(requestMessage(), this.result);
    }

    @Override
    protected void resolverRequestMessage(GpsMessage requestMessage) {
        byte[] bodyMessage = requestMessage.getBody().getBodyMessage();

        this.status = getStatus(ByteUtil.bytesToHex(bodyMessage, 0, 1));

        this.statusTime = ByteUtil.bcdToStr(bodyMessage, 1, 6);

        this.icResult = getIcResult(ByteUtil.bytesToHex(bodyMessage, 7, 1));

        byte nameLength = bodyMessage[8];

        this.name = ByteUtil.bytesToString(bodyMessage, 9, nameLength + 9);

        this.sacNo = ByteUtil.bytesToString(bodyMessage, nameLength + 9, nameLength + 29);

        byte tcbLength = bodyMessage[nameLength + 29];

        this.tcbName = ByteUtil.bytesToString(bodyMessage, 30 + nameLength, nameLength + 30 + tcbLength);
    }

    private IcResult getIcResult(String bytesToHex) {
        switch (bytesToHex) {
            case "00": return IcResult.success;
            case "01": return IcResult.unAuth;
            case "02": return IcResult.out;
            case "03": return IcResult.failed;
        }
        return null;
    }

    private DIdentityStatus getStatus(String bytesToHex) {
        switch (bytesToHex) {
            case "01" : return DIdentityStatus.in;
            case "02" : return DIdentityStatus.out;
        }
        return null;
    }

    @Override
    public GpsProtocolType protocolType() {
        return GpsProtocolType.DIdentity;
    }

    public String getName() {
        return name;
    }

    public DIdentityProtocol setName(String name) {
        this.name = name;
        return this;
    }

    public String getSacNo() {
        return sacNo;
    }

    public DIdentityProtocol setSacNo(String sacNo) {
        this.sacNo = sacNo;
        return this;
    }

    public String getTcbName() {
        return tcbName;
    }

    public DIdentityProtocol setTcbName(String tcbName) {
        this.tcbName = tcbName;
        return this;
    }

    public GpsCommonRespResult getResult() {
        return result;
    }

    public DIdentityProtocol setResult(GpsCommonRespResult result) {
        this.result = result;
        return this;
    }

    @Override
    public String toString() {
        return "DIdentityProtocol{" +
                "status=" + status +
                ", statusTime='" + statusTime + '\'' +
                ", icResult=" + icResult +
                ", name='" + name + '\'' +
                ", sacNo='" + sacNo + '\'' +
                ", tcbName='" + tcbName + '\'' +
                '}';
    }
}
