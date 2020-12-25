package com.iteaj.network.protocol;

/**
 *  <p>协议类型</p>
 * 协议头里面的交易码
 *
 * Create Date By 2017-09-08
 * @author iteaj
 * @since 1.7
 */
public enum ProtocolType {
    Common_Heart_Type(88888, "心跳协议"), // 通用的心跳协议
    /* 设备响应的协议没有找到对应的平台请求报文协议 */
    NoneMap(95555, "设备响应的报文找不到对应的平台报文"),
    /* 本地使用的协议 六位数 以9开头*/
    TC900000(90000, "事件管理协议 - 用来管理设备断线,重连等事件操作"),
    TCClint(99999, "后台管理客户端协议");
    public int value; //交易码值
    public String desc; //交易描述
    ProtocolType(int value, String desc) {
        this.desc = desc;
        this.value = value;
    }

    /**
     * @see #value 通过值获取一个实例
     * @param value
     * @return
     */
    public static ProtocolType getInstance(int value){
        switch (value){
            /* 设备协议 */

            default: return null; //throw new IllegalArgumentException("未知的交易码："+ value);
        }
    }

}
