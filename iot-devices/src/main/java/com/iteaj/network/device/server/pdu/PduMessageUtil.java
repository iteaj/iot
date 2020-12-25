package com.iteaj.network.device.server.pdu;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;

public class PduMessageUtil {

    public static String getId(String[] content, int index) {
        return content[index];
    }

    public static boolean startWith(String[] content, int index, String start) {
        String string = content[index];
        return string.startsWith(start);
    }

    public static String getString(String[] content, int index) {
        String value = content[index].split("=")[1];
        return value.substring(1, value.length()-1);
    }

    public static Integer getInteger(String[] content, int index) {
        String string = getString(content, index);
        return Integer.valueOf(string);
    }

    public static BigDecimal getBigDecimal(String[] content, int index) {
        String string = getString(content, index);
        return new BigDecimal(string);
    }

    public static char[] getBytes(String[] content, int index) {
        Integer integer = getInteger(content, index);
        String s = Integer.toBinaryString(integer);
        return s.toCharArray();
    }

    public static String endString(String[] content, int index) {
        String value = content[content.length - index].split("=")[1];
        return value.substring(1, value.length()-1);
    }

    /**
     * 生成校验码(不计算前后空格)
     * @param str：
     * @return
     */
    public static int getCode(String str) {
        String[] strings = str.split(" ");
        String[] subarray = ArrayUtils.subarray(strings, 1, strings.length - 2);
        byte chars[] = String.join(" ", subarray).getBytes();
        int sum = 0;
        for (int i = 0; i < chars.length; i++) {
            sum = sum + chars[i];
        }
        sum = sum & 0xff;
        return sum;
    }
}
