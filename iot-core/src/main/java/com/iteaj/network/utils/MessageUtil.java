package com.iteaj.network.utils;

import com.iteaj.network.protocol.ProtocolType;
import com.iteaj.network.message.DeviceMessageBody;
import com.iteaj.network.message.DeviceMessageHead;
import org.apache.commons.lang3.RandomUtils;

/**
 * Create Date By 2017-09-13
 *
 * @author iteaj
 * @since 1.7
 */
public final class MessageUtil {

    private static String MD5_KEY;

    static {
        try {
//            MD5_KEY = PropertyUtils.getProperty("device.md5.key");
        } catch (Exception e) {
            MD5_KEY = "zhfreeview";
        }
    }

    public static DeviceMessageHead byteArrayToMessageHead(byte[] messageHead) {
        DeviceMessageHead head = new DeviceMessageHead(messageHead, null, null);
//        head.setStatus(TradeStatus.getInstance(ByteUtil.subBytes(messageHead, 4, 5)[0]));
        head.setMessageId(ByteUtil.subBytesToString(messageHead,13,29));
        head.setEquipCode(ByteUtil.subBytesToString(messageHead,33,49));
        return head;
    }

    public static DeviceMessageHead buildMessageHead(String equipCode, ProtocolType code
            , int headLength, int bodyLength){
        return buildMessageHead(getMessageId(), equipCode, code, headLength, bodyLength);
    }

    public static DeviceMessageHead buildMessageHead(String messageId,String equipCode
            , ProtocolType code, int headLength, int bodyLength){
        DeviceMessageHead head = new DeviceMessageHead(null, null, null);
        int nextInt = RandomUtils.nextInt();
        head.setEquipCode(equipCode);
//        head.setStatus(TradeStatus.请求);
        head.setMessageId(messageId);
        messageHeadToByteArray(head);
        return head;
    }

    public static DeviceMessageHead buildResponseHeadFormRequestHead(DeviceMessageHead requestHead
            , int reserved, int bodyLength){
        if(null == requestHead)
            throw new IllegalArgumentException("设备的请求报文头(requestHead)不能为空");

        byte[] reqBytes = new byte[requestHead.getHeadLength()];
        int nextInt = RandomUtils.nextInt();
        DeviceMessageHead deviceMessageHead = new DeviceMessageHead(null, null, null);
        deviceMessageHead.setMessageId(requestHead.getMessageId());
//        deviceMessageHead.setStatus(TradeStatus.应答);
        deviceMessageHead.setEquipCode(requestHead.getEquipCode());
        messageHeadToByteArray(deviceMessageHead);
        return deviceMessageHead;
    }

    public static DeviceMessageHead buildResponseHeadFormRequestHead(DeviceMessageHead requestHead
            , int reserved, DeviceMessageBody messageBody){
        return buildResponseHeadFormRequestHead(requestHead, reserved, messageBody.getBodyLength());
    }

    public static byte[] messageHeadToByteArray(DeviceMessageHead requestHead){
        byte[] reqBytes = new byte[requestHead.getHeadLength()];
//        reqBytes[4] = requestHead.getStatus().value;
        ByteUtil.addBytes(reqBytes, requestHead.getMessageId().getBytes(),13);
        ByteUtil.addBytes(reqBytes, requestHead.getEquipCode().getBytes(),33);

        return reqBytes;
    }

    public static synchronized String getMessageId(){
        return String.valueOf(System.currentTimeMillis());
    }
}
