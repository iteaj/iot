package com.iteaj.network.device.client.breaker.fzwu;

import cn.hutool.core.date.DateUtil;
import com.iteaj.network.utils.ByteBufUtil;
import com.iteaj.network.utils.ByteUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class BreakerUtils {

    /**
     * 根据断路器请求的字节报文构建报文对象
     * @param payload
     * @param topic
     * @return
     */
    public static BreakerMessage buildBreakerRequestMessage(byte[] payload, String topic) {
        BreakerMessage breakerMessage = new BreakerMessage(payload, topic);
        breakerMessage.setGateway(getGatewayOfTopic(topic));
        breakerMessage.setHeader(ByteBufUtil.byteToHex(payload, 0));
        breakerMessage.setLen(ByteBufUtil.hexToShort(payload, 1));

        breakerMessage.setCtrl(ByteBufUtil.byteToHex(payload, 3));
        breakerMessage.setAddr(ByteBufUtil.bytesToHex(payload, 4, 6));
        breakerMessage.setType(ByteBufUtil.byteToHex(payload, 10));
        breakerMessage.setData(ArrayUtils.subarray(payload, 11, payload.length - 8));
        breakerMessage.setTime(ByteUtil.bcdToStr(ArrayUtils.subarray(payload, payload.length - 8, payload.length - 2)));
        breakerMessage.setCs(ByteBufUtil.byteToHex(payload, payload.length - 2));
        breakerMessage.setOver(ByteBufUtil.byteToHex(payload, payload.length - 1));

        return breakerMessage;
    }

    public static String getGatewayOfTopic(String topic) {
        return topic.split("/")[2];
    }

    public static byte[] doBuildPayload(BreakerMessage responseMessage) {
        int dataLength = responseMessage.getData().length;
        byte[] bytes = new byte[dataLength + 19];

        // 长度站两个字节
        int len = dataLength + 14;
        String lenHex = ByteUtil.intToHex(len);
        byte[] lenBytes = ByteBufUtil.hexToBytes(lenHex);
        if(lenBytes.length == 1) lenBytes = new byte[]{0, lenBytes[0]};

        responseMessage.setLen((short) len);
        ByteUtil.addBytes(bytes, ByteBufUtil.hexToBytes(responseMessage.getHeader()), 0); // 头
        ByteUtil.addBytes(bytes, lenBytes, 1); // 设置长度
        ByteUtil.addBytes(bytes, ByteBufUtil.hexToBytes("80"), 3); // 控制域是 80H 从服务到网关
        ByteUtil.addBytes(bytes, ByteBufUtil.hexToBytes(responseMessage.getAddr()), 4); // 地址域
        ByteUtil.addBytes(bytes, ByteBufUtil.hexToBytes(responseMessage.getType()), 10); // 功能码
        ByteUtil.addBytes(bytes, responseMessage.getData(), 11); // 数据域
        ByteUtil.addBytes(bytes, ByteUtil.str2Bcd(responseMessage.getTime()), 11 + dataLength); // 数据时效

        ByteUtil.addBytes(bytes, byteValue(bytes), bytes.length - 2);
        ByteUtil.addBytes(bytes, ByteUtil.hexToByte(responseMessage.getOver()), bytes.length - 1);

        return bytes;
    }

    public static int hexToInt(byte[] data, int offset, int length) {
        String hex = ByteUtil.bytesToHex(data, offset, length);
        return Integer.valueOf(hex, 16);
    }

    // 控制域到数据时效的累加和
    private static byte[] byteValue(byte[] bytes) {
        byte[] subarray = ArrayUtils.subarray(bytes, 3, bytes.length - 2);
        int sum = 0;
        for (int i=0; i<subarray.length; i++) {
            String s = ByteBufUtil.byteToHex(subarray[i]);
            sum += Integer.valueOf(s, 16);
        }

        String toHexString = Integer.toHexString(sum);
        String substring = toHexString.substring(toHexString.length() - 2);
        return ByteBufUtil.hexToBytes(substring);
    }

    public static String getChildType(int type) {
        switch (type) {
            case 0: return "过压异常";
            case 1: return "欠压异常";
            case 2: return "过流异常";
            case 3: return "开关状态异常";
            case 4: return "漏电异常";
            case 5: return "短路异常";
            case 6: return "温度过高异常";
            case 7: return "线路打火异常";
            case 8: return "最大功率异常";
            case 9: return "最小功率异常";
            case 10: return "漏电功能已坏";
            case 11: return "进入维修模式";
            case 12: return "断零异常";
            case 13: return "三相不平衡";
            case 14: return "缺相异常";
            case 15: return "锁死异常";
            case 16: return "过压预警";
            case 17: return "欠压预警";
            case 18: return "过流预警";
            case 19: return "温度预警";
            case 99: return "其他异常";
            default: return "";
        }
    }

    /**
     * @see #doBuildPayload(BreakerMessage) 在此处理len字段
     * @param type
     * @param addr
     * @return
     */
    public static BreakerMessage buildBreakerRequestMessage(String type, String addr, byte[] data) {
        BreakerMessage breakerMessage = new BreakerMessage();
        breakerMessage.setType(type);
        breakerMessage.setOver("16");
        breakerMessage.setHeader("68");
        breakerMessage.setData(data);
        String time = DateUtil.format(new Date(), "yyMMddHHmmss");
        breakerMessage.setTime(time);
        breakerMessage.setAddr(addr);
        breakerMessage.setCtrl("80"); // 服务发送为：80H，LINK 发送为： 81H，APP 发送为：82H
        return breakerMessage;
    }

    public static BreakerMessage buildBreakerMessageFormRequestMessage(BreakerMessage requestMessage) {
        BreakerMessage breakerMessage = new BreakerMessage(requestMessage.getTopic());
        breakerMessage.setData(new byte[]{});
        BeanUtils.copyProperties(requestMessage, breakerMessage, "data");
        return breakerMessage;
    }

    public String getCtrl(byte[] p) {
        return null;
    }

    /**
     * 加密
     * @param encKey
     * @param intCmd
     * @return
     */
    public static String encCmd(String encKey, int[] intCmd) {
        int cmdLen = intCmd.length; int[] intKey = new int[encKey.length()]; for (int i = 0; i < encKey.length(); i++){
            intKey[i] = encKey.charAt(i);}
        for (int i = 0; i < cmdLen; i++){
            intCmd[i] = (intCmd[i] + intKey[i % encKey.length()]) % 0x100;}
        String encHexCmd = "";
        for (int i = 0; i < cmdLen; i++) {
            encHexCmd += intToHexStr(intCmd[i], 2)+" ";
        }
        return encHexCmd;
    }

    /**
     * 解密
     * @param encKey
     * @param intCmd
     * @return
     */
    public static String decCmd(String encKey, int[] intCmd) {
        int cmdLen = intCmd.length;
        int[] intKey = new int[encKey.length()];
        for (int i = 0; i < encKey.length(); i++)
            intKey[i] = encKey.charAt(i);

        for (int i = 0; i < cmdLen; i++){
            intCmd[i] = (intCmd[i] - intKey[i % encKey.length()] + 0x100) % 0x100;
        }
        String decHexCmd = "";
        for (int i = 0; i < cmdLen; i++) {
            decHexCmd += intToHexStr(intCmd[i], 2)+" ";
        }
        return decHexCmd;
    }

    public static String intToHexStr(int source, int len) {
        String hexStr = String.format("%" + (len > 0 ? len : "") + "s",//
                Integer.toHexString(source)).replace(' ', '0');
        return (len == 0) ? hexStr : //
                hexStr.substring(hexStr.length() - len, hexStr.length());
    }

    // 16进制字符串转整形
    public static int hexStrToInt(String hexStr) {
        try {
            return Integer.valueOf(hexStr, 16);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int handleErrCode(byte[] subBytes) {

        int value = ByteUtil.bytesToInt(subBytes);
        for(int i=0; i< 32; i++) {
            if(getBitStatus(value, i)) {
                return i;
            }
        }

        return -1;
    }

    private static boolean getBitStatus(int num, int pos) {
        if((num & (1 << pos)) != 0) { //按位与之后的结果非0
            return true;
        } else {
            return false;
        }
    }

}
