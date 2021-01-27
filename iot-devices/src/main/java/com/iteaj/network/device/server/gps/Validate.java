package com.iteaj.network.device.server.gps;

/**
 * create time: 2021/1/22
 *
 * @author iteaj
 * @since 1.0
 */
public class Validate {

    public static byte getXor(byte[] datas){

        byte temp=datas[0];

        for (int i = 1; i <datas.length; i++) {
            temp ^=datas[i];
        }

        return temp;
    }
}
