package com.iteaj.network;

/**
 * Create Date By 2020-09-11
 * @author iteaj
 * @since 1.7
 */
public interface Message {

    /**
     * 返回报文长度
     * @return
     */
    int length();

    /**
     * 返回报文字节数
     * @return
     */
    byte[] getMessage();

    /**
     * 返回报文头
     * @return
     */
    MessageHead getHead();

    /**
     * 返回报文体
     * @return
     */
    MessageBody getBody();

    /**
     * 报文头
     */
    interface MessageHead {

        /**
         * 设备编号
         * @return
         */
        String getEquipCode();

        /**
         * 报文的唯一编号
         * @return
         */
        String getMessageId();

        /**
         * 获取交易类型
         * @return
         */
        <T> T getTradeType();

        /**
         * 获取报文头长度
         * @return
         */
        int getHeadLength();

        /**
         * 报文头数据
         * @return
         */
        byte[] getHeadMessage();
    }

    /**
     * 报文体
     */
    interface MessageBody {
        /**
         * 获取报文体长度
         * @return
         */
        int getBodyLength();

        /**
         * 报文体数据
         * @return
         */
        byte[] getBodyMessage();
    }
}
