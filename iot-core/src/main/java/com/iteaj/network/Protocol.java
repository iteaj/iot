package com.iteaj.network;

/**
 * Create Date By 2017-09-11
 * @author iteaj
 * @since 1.7
 */
public interface Protocol {

    /**
     * 返回协议类型
     * @return
     */
    <T> T protocolType();

    /**
     * 协议描述
     * @return
     */
    String desc();

    /**
     * 获取此协议对应设备的设备编号
     * @return
     */
    String getEquipCode();

    /**
     * 新增额外参数
     * @param key
     * @param value
     * @return
     */
    Protocol addParam(String key, Object value);

    /**
     * 移除参数
     * @param key
     * @return
     */
    Protocol removeParam(String key);

    /**
     * 获取参数
     * @param key
     * @return
     */
    Object getParam(String key);

    /**
     * 返回请求报文
     * @return
     */
    Message requestMessage();

    /**
     * 返回响应报文
     * @return
     */
    Message responseMessage();
}
