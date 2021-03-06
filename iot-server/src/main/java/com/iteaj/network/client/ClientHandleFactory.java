package com.iteaj.network.client;

import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.handle.MethodMeta;

import java.lang.reflect.Method;

/**
 * create time: 2021/3/4
 *
 * @author iteaj
 * @since 1.0
 */
public interface ClientHandleFactory {

    MethodMeta getHandle(String tradeType);

    ParamResolver getResolver(Class<? extends ParamResolver> resolver);

    ClientRelation getRelation(AppClientMessage message);
}
