package com.iteaj.network.client.handle;

import com.alibaba.fastjson.util.TypeUtils;
import com.iteaj.network.client.ParamResolver;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppJsonMessageBody;

/**
 * create time: 2021/3/4
 *
 * @author iteaj
 * @since 1.0
 */
public class JsonResolver implements ParamResolver {

    private static final String DEVICE_SN = "deviceSn";

    @Override
    public String getDeviceSn(AppClientMessage message) {
        return message.getDeviceSn();
    }

    @Override
    public String getTradeType(AppClientMessage message) {
        return message.getHead().getTradeType();
    }

    /**
     * 1. 如果名称为deviceSn则返回设备编号
     * 2. 不支持解析非包装的基本数据类型 比如：int, double 请使用Integer类型替代
     * 3. 如果名称为body则直接返回
     * @param name
     * @param type
     * @param message
     * @return
     */
    @Override
    public Object resolver(String name, Class type, AppClientMessage message) {
        if(type == String.class && DEVICE_SN.equals(name)) {
            return message.getDeviceSn();
        }

        if(message.getBody() instanceof AppJsonMessageBody) {
            try {
                return this.bodyTypeParamResolver(name, type, (AppJsonMessageBody)message.getBody());
            } catch (Exception e) {
                throw new IllegalArgumentException("不能解析名为["+name+"]的参数["+type+"]");
            }
        }

        return null;
    }

    private Object bodyTypeParamResolver(String name, Class type, AppJsonMessageBody body) {
        return TypeUtils.castToJavaBean(body.get(name), type);
    }
}
