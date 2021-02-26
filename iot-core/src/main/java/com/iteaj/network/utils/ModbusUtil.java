package com.iteaj.network.utils;

public class ModbusUtil {

    /**
     * 高位在前，低位在后
     * @param bytes
     * @return
     */
    public static String getCRC(byte[] bytes) {
        return getCRC(bytes, false);
    }

    /**
     * @param bytes
     * @param lb 是否低位在前, 高位在后
     * @return
     */
    public static String getCRC(byte[] bytes, boolean lb) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }

        //结果转换为16进制
        String result = Integer.toHexString(CRC).toUpperCase();
        if (result.length() != 4) {
            StringBuffer sb = new StringBuffer("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }

        if(lb) { // 低位在前, 高位在后
            result = result.substring(2, 4) + result.substring(0, 2);
        }

        return result;
    }
}
