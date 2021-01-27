package com.iteaj.network.device.server.gps;

import com.iteaj.network.Message;
import com.iteaj.network.utils.ByteUtil;

/**
 * create time: 2021/1/19
 *
 * @author iteaj
 * @since 1.0
 */
public class GpsMessageHead implements Message.MessageHead {


    private byte[] message;
    // 消息流水号
    private String messageId;
    // 设备编号/终端手机号
    private String equipCode;
    // 协议类型
    private GpsProtocolType type;

    // 消息体属性
    private String bodyAttr;

    private final static String EmptyBinary= "0000000000000000";

    protected GpsMessageHead(String equipCode, String messageId, GpsProtocolType type, String bodyAttr) {
        this.type = type;
        this.equipCode = equipCode;
        this.messageId = messageId;
        this.bodyAttr = buildBodyAttr(bodyAttr);
    }

    /**
     * 构建响应报文
     * @param equipCode 设备编号或者手机号
     * @param messageId
     * @param type
     * @param bodyLength 消息体长度
     * @return
     */
    public static GpsMessageHead resp(String equipCode, String messageId, GpsProtocolType type, int bodyLength) {
        GpsMessageHead messageHead = new GpsMessageHead(equipCode, messageId, type, Integer.toBinaryString(bodyLength));
        byte[] message = new byte[12];

        ByteUtil.addBytes(message, ByteUtil.hexToByte(type.code), 0);
        ByteUtil.addBytes(message, ByteUtil.shortToBytes(Integer.valueOf(bodyLength).shortValue()), 2);
        ByteUtil.addBytes(message, ByteUtil.str2Bcd(equipCode), 4);
        Integer id = Integer.valueOf(messageId) + 1;
        ByteUtil.addBytes(message, ByteUtil.shortToBytes(id.shortValue()), 10);

        System.out.println("响应报文头" + ByteUtil.bytesToHex(message));
        messageHead.message=message;
        return messageHead;
    }

    /**
     * 构建请求报文
     * @param message
     * @return
     */
    public static GpsMessageHead build(byte[] message) {
        GpsProtocolType type = getProtocolType(ByteUtil.bytesToHex(message, 0, 2));

        String equipCode = ByteUtil.bcdToStr(message, 4, 6);

        String messageId = ByteUtil.bytesToShort(message, 10) + "";

        String binaryString = Integer.toBinaryString(ByteUtil.bytesToShort(message, 2));
        GpsMessageHead gpsMessageHead = new GpsMessageHead(equipCode, messageId, type, binaryString);
        gpsMessageHead.message = message;

        return gpsMessageHead;
    }

    private static String buildBodyAttr(String binaryString) {
        String substring = EmptyBinary.substring(0, 16 - binaryString.length());
        return substring+binaryString;
    }

    private static GpsProtocolType getProtocolType(String bytesToHex) {
        switch (bytesToHex) {
            case "0001": return GpsProtocolType.TResp;
            case "0002": return GpsProtocolType.Heart;
            case "0003": return GpsProtocolType.TLogout;
            case "0102": return GpsProtocolType.TAuth;
            case "0100": return GpsProtocolType.TRegister;
            case "0200": return GpsProtocolType.PReport;
            case "8001": return GpsProtocolType.PResp;
            case "8100": return GpsProtocolType.RegResp;
            case "8103": return GpsProtocolType.STParam;
            case "8104": return GpsProtocolType.QTParam;
            case "0104": return GpsProtocolType.QTRParam;
            case "8105": return GpsProtocolType.TCtrl;
            case "8201": return GpsProtocolType.QPInfo;
            case "0201": return GpsProtocolType.QPRInfo;
            case "0702": return GpsProtocolType.DIdentity;
            default: return GpsProtocolType.Unknown;
        }
    }

    @Override
    public String getEquipCode() {
        return this.equipCode;
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public GpsProtocolType getTradeType() {
        return this.type;
    }

    @Override
    public int getHeadLength() {
        return this.message.length;
    }

    @Override
    public byte[] getHeadMessage() {
        return this.message;
    }

    public String getBodyAttr() {
        return bodyAttr;
    }

    /**
     * 声明RSA加密
     */
    protected void setRsa() {
        char[] chars = this.bodyAttr.toCharArray();
        chars[5] = '1';
        this.bodyAttr = new String(chars);
    }

    /**
     * 是否RSA加密
     * @return
     */
    public boolean isRsa() {
        return bodyAttr.substring(3, 6).equals("000");
    }

    /**
     * 声明此报文分包
     */
    protected void subcontract() {
        char[] chars = this.bodyAttr.toCharArray();
        chars[2] = '1';
        this.bodyAttr = new String(chars);
    }

    /**
     * 是否分包
     * @return
     */
    public boolean isSubcontract() {
        return bodyAttr.charAt(2) == '1';
    }

    @Override
    public String toString() {
        return "GpsMessageHead{" +
                "equipCode='" + equipCode + '\'' +
                ", messageId='" + messageId + '\'' +
                ", type=" + type +
                '}';
    }
}
