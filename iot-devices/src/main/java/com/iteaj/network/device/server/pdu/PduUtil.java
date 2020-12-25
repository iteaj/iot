package com.iteaj.network.device.server.pdu;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class PduUtil {

    /**
     * 计算要执行的插座口
     * @param totalSlot 总插座口
     * @param selectSlots 要操作的插座口
     * @return 如:cport='135' 135=1000 0111,所以选择的插座口为 123----8
     */
    public static String getPort(int totalSlot, List<Integer> selectSlots) {
        if(CollectionUtils.isEmpty(selectSlots)) return "0";

        char[] port = new char[totalSlot];
        for(int i=0; i<totalSlot; i++) {
            if(selectSlots.contains(i+1)) {
                port[totalSlot - i - 1] = '1';
            } else {
                port[totalSlot - i - 1] = '0';
            }
        }
        int parseInt = Integer.parseInt(new String(port), 2);
        return String.valueOf(parseInt);
    }
}
