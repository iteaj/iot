package com.iteaj.network.utils;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ByteBufUtil {

    public  static byte[] copyByteBuf(ByteBuf byteBuf){
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static short bytesToShort(byte[] src, int offset) {
        short value = 0;
//        value = Integer((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) );
        return value;
    }

    public static short getShort(byte[] src, int offset) {
        return (short) (((src[offset] & 0xff) | (src[offset+1] << 8)));
    }


    /**
     * 字节转十六进制
     * @param b 需要进行转换的byte字节
     * @return  转换后的Hex字符串
     */
    public static String byteToHex(byte b){
        int i = b & 0xFF;
        String hex = Integer.toHexString(i);
        if(hex.length() < 2){
            hex = "0" + hex;
        }
        return hex;
    }

    public static String byteToHex(byte[] src, int offset) {
        return byteToHex(src[offset]);
    }

    public static String bytesToBCD(byte[] src, int offset, int length) {
        return bytesToHex(src, offset, length);
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexToBytes(String hexStr) {
        if (hexStr.length() < 1) return null;

        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static int[] hexToInts(String hexStr) {
        if (hexStr.length() < 1) return null;
        int[] result = new int[hexStr.length() / 2];

        for (int i = 0; i < hexStr.length() / 2; i++) {
            String substring = hexStr.substring(i * 2, i * 2 + 2);
            result[i] = Integer.parseInt(substring, 16);
        }

        return result;
    }

    /**
     * 字节数组转16进制
     * @param src 需要转换的byte数组
     * @return  转换后的Hex字符串
     */
    public static String bytesToHex(byte[] src, int offset, int length) {
        StringBuffer sb = new StringBuffer();
        byte[] bytes = ArrayUtils.subarray(src, offset, offset + length);
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static short hexToShort(byte[] src, int offset) {
        String s = bytesToHex(src, offset, 2);
        return Short.parseShort(s, 16);
    }

    /**
     * Hex字符串转byte
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte
     */
    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }
}
