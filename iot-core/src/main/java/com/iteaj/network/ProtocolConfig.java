package com.iteaj.network;

import org.springframework.beans.factory.annotation.Value;

/**
 * <h3>协议相关配置信息</h3>
 * Create Date By 2017-09-06
 * @author iteaj
 * @since 1.7
 */
public class ProtocolConfig {
    /**心跳间隔时间*/
    public static int HEART_TIME;
    /**协议MD5加密key*/
    public static String KEY;
    /**协议服务端主机*/
    public static String SERVER_HOST;
    /**平台FTP根路径*/
    public static String FTP_URL;
    /** 报文的最大帧长度 */
    public static int MAX_FRAME_LENGTH;
    /** 设备二维码的内容 */
//    public static String QRCODE_URL;

    @Value("${protocol.heart.time}")
    public void setHeartTime(int heartTime) {
        HEART_TIME = heartTime;
    }

    @Value("${protocol.md5.key}")
    public void setKEY(String KEY) {
        ProtocolConfig.KEY = KEY;
    }

//    @Value("${protocol.web.host}")
    public void setServerHost(String serverHost){
        ProtocolConfig.SERVER_HOST = serverHost;
    }

//    @Value("${protocol.ftp.url}")
    public void setFtpUrl(String ftpUrl){
        FTP_URL = ftpUrl;
    }

    @Value("${protocol.max.frame}")
    public void setMaxFrameLength(int maxFrameLength){
        MAX_FRAME_LENGTH = maxFrameLength;
    }
//    @Value("${protocol.qrcode.url}")
//    public void setQrcodeUrl(String qrcodeUrl){
//        QRCODE_URL = qrcodeUrl;
//    }
}
