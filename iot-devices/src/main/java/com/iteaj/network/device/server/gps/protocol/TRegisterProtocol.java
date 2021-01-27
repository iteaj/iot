package com.iteaj.network.device.server.gps.protocol;

import com.iteaj.network.Message;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.device.server.gps.GpsMessage;
import com.iteaj.network.device.server.gps.GpsMessageBody;
import com.iteaj.network.device.server.gps.GpsMessageHead;
import com.iteaj.network.device.server.gps.GpsProtocolType;
import com.iteaj.network.utils.ByteUtil;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;

/**
 * create time: 2021/1/20
 *  终端注册协议
 * @author iteaj
 * @since 1.0
 */
public class TRegisterProtocol extends GpsDeviceRequestProtocol {

    // 市县域ID
    private String city;
    // 省域ID
    private String province;
    // 制造商ID
    private String mfrs;
    // 终端型号
    private String terminalSeries;
    // 终端ID
    private String tid;
    // 车牌颜色
    private String carColor;
    // 车牌号
    private String carNo;

    // 鉴权码
    private String authCode;

    public TRegisterProtocol(GpsMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected GpsMessage doBuildResponseMessage() {
        try {
            GpsMessageHead requestHead = (GpsMessageHead)requestMessage().getHead();

            if(!StringUtils.hasText(authCode) || authCode.length() < 3) {
                throw new IllegalArgumentException("鉴权码长度必须大于三个字符");
            }

            byte[] bodyMessage = new byte[3 + authCode.length()];
            ByteUtil.addBytes(bodyMessage, ByteUtil.shortToBytes(Short.valueOf(requestHead.getMessageId())), 0);
            bodyMessage[2] = 0;

            ByteUtil.addBytes(bodyMessage, authCode.getBytes(Charset.forName("utf-8")), 3);
            GpsMessageBody body = new GpsMessageBody(bodyMessage);

            // 构建响应报文头
            GpsMessageHead responseHead = GpsMessageHead.resp(requestHead.getEquipCode()
                    , requestHead.getMessageId(), GpsProtocolType.RegResp, 6);

            GpsMessage gpsMessage = new GpsMessage(responseHead, body);
            System.out.println("响应报文："+ByteUtil.bytesToHex(gpsMessage.getMessage()));
            return gpsMessage;
        } catch (NumberFormatException e) {
            throw new ProtocolException("数字格式化异常", e);
        }
    }

    @Override
    protected void resolverRequestMessage(GpsMessage requestMessage) {
        Message.MessageBody body = requestMessage.getBody();
        byte[] message = body.getBodyMessage();
        System.out.println(ByteUtil.bytesToHex(message));
        this.province = ByteUtil.bytesToHex(message, 0, 2);
        this.city = ByteUtil.bytesToHex(message, 2, 2);

        byte[] mfrs = ByteUtil.subBytes(message, 4, 9);
        this.mfrs = new String(mfrs);

        byte[] ts = ByteUtil.subBytes(message, 9, 17);
        this.terminalSeries = new String(ts);

        this.tid = ByteUtil.bytesToHex(message, 17, 7);
        this.carColor = ByteUtil.bytesToHex(message, 24, 1);

        byte[] carNo = ByteUtil.subBytes(message, 25, message.length - 1);
        this.carNo = new String(carNo);

        System.out.println(this);
    }

    @Override
    public GpsProtocolType protocolType() {
        return GpsProtocolType.TRegister;
    }

    public String getCity() {
        return city;
    }

    public TRegisterProtocol setCity(String city) {
        this.city = city;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public TRegisterProtocol setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getMfrs() {
        return mfrs;
    }

    public TRegisterProtocol setMfrs(String mfrs) {
        this.mfrs = mfrs;
        return this;
    }

    public String getTerminalSeries() {
        return terminalSeries;
    }

    public TRegisterProtocol setTerminalSeries(String terminalSeries) {
        this.terminalSeries = terminalSeries;
        return this;
    }

    public String getTid() {
        return tid;
    }

    public TRegisterProtocol setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public String getCarColor() {
        return carColor;
    }

    public TRegisterProtocol setCarColor(String carColor) {
        this.carColor = carColor;
        return this;
    }

    public String getCarNo() {
        return carNo;
    }

    public TRegisterProtocol setCarNo(String carNo) {
        this.carNo = carNo;
        return this;
    }

    public String getAuthCode() {
        return authCode;
    }

    public TRegisterProtocol setAuthCode(String authCode) {
        this.authCode = authCode;
        return this;
    }

    @Override
    public String toString() {
        return "TRegisterProtocol{" +
                "city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", mfrs='" + mfrs + '\'' +
                ", terminalSeries='" + terminalSeries + '\'' +
                ", tid='" + tid + '\'' +
                ", carColor='" + carColor + '\'' +
                ", carNo='" + carNo + '\'' +
                '}';
    }
}
