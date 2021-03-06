package com.iteaj.network.client;

import com.iteaj.network.client.app.AppClientMessage;

/**
 * create time: 2021/3/4
 *  参数解析器, 非线程安全
 * @author iteaj
 * @since 1.0
 */
public interface ParamResolver {

    String getDeviceSn(AppClientMessage message);

    String getTradeType(AppClientMessage message);

    Object resolver(String name, Class type, AppClientMessage message);
}
