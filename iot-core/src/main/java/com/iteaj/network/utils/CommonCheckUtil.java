package com.iteaj.network.utils;

public class CommonCheckUtil {

    /**
     * xor 校验
     * @param datas
     * @return
     */
    public static byte getXor(byte[] datas) {
        byte temp = 0;
        for (int i = 0; i < datas.length; i++) {

            temp ^= (datas[i]);
        }
        return temp;
    }

    public static void main(String[] args) {
        byte[] bytes = ByteUtil.hexToByte("02001B13");
        byte xor = getXor(bytes);
        System.out.println(ByteUtil.byteToHex(xor));
    }
}
