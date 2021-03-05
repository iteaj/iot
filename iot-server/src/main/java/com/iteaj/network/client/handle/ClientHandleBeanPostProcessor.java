package com.iteaj.network.client.handle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandleBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Method> clientMapping = new ConcurrentHashMap();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final ClientHandle clientHandle = bean.getClass().getAnnotation(ClientHandle.class);
        if(clientHandle != null) {
            final String value = clientHandle.value();
            ReflectionUtils.doWithMethods(bean.getClass(), item -> {
                final IotMapping iotMapping = item.getAnnotation(IotMapping.class);
                if(iotMapping == null) {
                    return;
                }

                String tradeType = value;
                if(iotMapping != null || iotMapping.value() != null) {
                    tradeType += iotMapping.value();
                }


                if(StringUtils.hasText(tradeType)) {
                    final Method method = clientMapping.get(tradeType);
                    if(method == null) {
                        clientMapping.put(tradeType, item);
                    } else {
                        throw new BeanInitializationException("错误的映射名称["+tradeType+"]" +
                                ", 和方法冲突["+method.getDeclaringClass().getName()+method.getName()+"]");
                    }
                } else {
                    throw new BeanInitializationException("错误的映射名称["+tradeType+"], 必须不能空");
                }
            });
        }

        return bean;
    }
}
