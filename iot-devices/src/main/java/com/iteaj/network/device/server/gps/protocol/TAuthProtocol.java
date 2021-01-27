package com.iteaj.network.device.server.gps.protocol;

import com.iteaj.network.Message;
import com.iteaj.network.device.server.gps.GpsCommonRespResult;
import com.iteaj.network.device.server.gps.GpsMessage;
import com.iteaj.network.device.server.gps.GpsProtocolType;

/**
 * create time: 2021/1/22
 *  鉴权协议
 * @author iteaj
 * @since 1.0
 */
public class TAuthProtocol extends GpsDeviceRequestProtocol{

    /**
     * 鉴权码
     * @see TRegisterProtocol#getAuthCode() 此鉴权码在注册的时候写个设备
     */
    private String authCode;

    /**
     * 鉴权结果, 用来响应给设备
     */
    private GpsCommonRespResult result;

    public TAuthProtocol(GpsMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected GpsMessage doBuildResponseMessage() {
        return GpsMessage.buildPlatformCommonRespMessageByRequest(this.requestMessage(), this.result);
    }

    @Override
    protected void resolverRequestMessage(GpsMessage requestMessage) {
        Message.MessageBody body = requestMessage.getBody();
        byte[] bodyMessage = body.getBodyMessage();

        this.authCode = new String(bodyMessage);
    }

    @Override
    public GpsProtocolType protocolType() {
        return GpsProtocolType.TAuth;
    }

    public GpsCommonRespResult getResult() {
        return result;
    }

    public TAuthProtocol setResult(GpsCommonRespResult result) {
        this.result = result;
        return this;
    }

    public String getAuthCode() {
        return authCode;
    }

    public TAuthProtocol setAuthCode(String authCode) {
        this.authCode = authCode;
        return this;
    }

    @Override
    public String toString() {
        return "TAuthProtocol{" +
                "authCode='" + authCode + '\'' +
                '}';
    }
}
