package com.iteaj.network.device.server.gps.protocol;

import com.iteaj.network.device.server.gps.GpsCommonRespResult;
import com.iteaj.network.device.server.gps.GpsMessage;
import com.iteaj.network.device.server.gps.GpsProtocolType;
import com.iteaj.network.utils.ByteUtil;

/**
 * create time: 2021/1/22
 *  未知信息上报协议
 * @author iteaj
 * @since 1.0
 */
public class PReportProtocol extends GpsDeviceRequestProtocol {

    /**
     * 报警标志位
     */
    private String warningFlag;

    /**
     * 状态
     */
    private String status;

    /**
     * 以度为单位的纬度值乘以10的6次方，精确到百万分之一度
     */
    private int lat;

    /**
     * 以度为单位的纬度值乘以10的6次方，精确到百万分之一度
     */
    private int lon;

    /**
     * 海拔高度，单位为米(m)
     */
    private int height;


    /**
     * 1/10km/h
     */
    private int speed;

    /**
     * 方向 0-359,正北为0，顺时针
     */
    private int dire;

    /**
     * YY-MM-DD-hh-mm-ss(GMT+8时间)
     */
    private String dateTime;

    /**
     * 未知信息上报结果
     */
    private GpsCommonRespResult result = GpsCommonRespResult.成功;

    private static final String EmptyBinary = "00000000000000000000000000000000";

    public PReportProtocol(GpsMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected GpsMessage doBuildResponseMessage() {
        return GpsMessage.buildPlatformCommonRespMessageByRequest(requestMessage(), result);
    }

    @Override
    protected void resolverRequestMessage(GpsMessage requestMessage) {
        byte[] bodyMessage = requestMessage.getBody().getBodyMessage();
        System.out.println("Gps经纬度协议体：" + ByteUtil.bytesToHex(bodyMessage));

        int bytesToInt = ByteUtil.bytesToInt(bodyMessage, 0);
        this.warningFlag = getIntBinary(bytesToInt);

        this.status = getIntBinary(ByteUtil.bytesToInt(bodyMessage, 4));

        this.lat = ByteUtil.bytesToInt(bodyMessage, 8);

        this.lon = ByteUtil.bytesToInt(bodyMessage, 12);

        this.height = ByteUtil.bytesToShort(bodyMessage, 16);

        this.speed = ByteUtil.bytesToShort(bodyMessage, 18);

        this.dire = ByteUtil.bytesToShort(bodyMessage, 20);

        this.dateTime = ByteUtil.bcdToStr(bodyMessage, 22, 6);
    }

    @Override
    public GpsProtocolType protocolType() {
        return GpsProtocolType.PReport;
    }

    protected String getIntBinary(int val) {
        String binaryString = Integer.toBinaryString(val);
        if(binaryString.length() == 32) return binaryString;

        String substring = EmptyBinary.substring(0, 32 - binaryString.length());

        return substring+binaryString;
    }

    @Override
    public String toString() {
        return "PReportProtocol{" +
                "warningFlag='" + warningFlag + '\'' +
                ", status='" + status + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", height=" + height +
                ", speed=" + speed +
                ", dire=" + dire +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
