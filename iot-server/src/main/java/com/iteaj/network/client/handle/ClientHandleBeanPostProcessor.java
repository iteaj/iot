package com.iteaj.network.client.handle;

import com.alibaba.fastjson.JSON;
import com.iteaj.network.client.ClientHandleFactory;
import com.iteaj.network.client.ClientRelation;
import com.iteaj.network.client.ParamResolver;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppJsonMessageBody;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandleBeanPostProcessor implements BeanPostProcessor, ClientHandleFactory {

    private final Map<String, MethodMeta> clientMapping = new ConcurrentHashMap();
    private final Map<Class<? extends ParamResolver>, ParamResolver> resolverMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final IotCtrl clientHandle = bean.getClass().getAnnotation(IotCtrl.class);
        if(clientHandle != null) {
            instanceParamResolver(clientHandle);

            final String value = clientHandle.value();
            ReflectionUtils.doWithMethods(bean.getClass(), item -> {
                final IotCtrl iotCtrl = item.getAnnotation(IotCtrl.class);
                if(iotCtrl == null) {
                    return;
                }

                String tradeType = value;
                if(iotCtrl != null || iotCtrl.value() != null) {
                    tradeType += iotCtrl.value();
                }

                if(StringUtils.hasText(tradeType)) {
                    MethodMeta methodMeta = clientMapping.get(tradeType);
                    if(methodMeta == null) {
                        instanceParamResolver(iotCtrl);
                        clientMapping.put(tradeType, new MethodMeta(item, bean, iotCtrl, clientHandle));
                    } else {
                        Method method = methodMeta.getMethod();
                        throw new BeanInitializationException("错误的映射名称["+tradeType+"]" +
                                ", 方法["+item.getDeclaringClass().getSimpleName()+"."+item.getName()+"()]和方法["
                                +method.getDeclaringClass().getSimpleName()+"."+method.getName()+"()]的@IotCtrl注解值冲突");
                    }
                } else {
                    throw new BeanInitializationException("错误的映射名称["+tradeType+"], 必须不能空");
                }
            });
        }

        return bean;
    }

    private void instanceParamResolver(IotCtrl clientHandle) {
        Class<? extends ParamResolver> resolver = clientHandle.resolver();
        if(!resolverMap.containsKey(resolver)) {
            try {
                resolverMap.put(resolver, resolver.newInstance());
            } catch (Exception e) {
                throw new BeanInitializationException(e.getMessage(), e);
            }
        }
    }

    @Override
    public MethodMeta getHandle(String tradeType) {
        return clientMapping.get(tradeType);
    }

    @Override
    public ClientRelation getRelation(AppClientMessage message) {
        String tradeType = message.getHead().getTradeType();
        MethodMeta handle = this.getHandle(tradeType);
        if(handle == null) {
            throw new IllegalArgumentException("找不到对应的方法["+tradeType+"], ");
        }

        Class resolverClazz = handle.getResolverClazz();
        ParamResolver resolver = this.getResolver(resolverClazz);

        Object[] values = null;
        Parameter[] parameters = handle.getMethod().getParameters();
        if(parameters != null && parameters.length > 0) {
            values = new Object[parameters.length];
            for(int i=0; i< parameters.length; i++) {
                Parameter parameter = parameters[i];

                Class<?> type = parameter.getType();
                IotBody iotBody = parameter.getDeclaredAnnotation(IotBody.class);
                if(iotBody == null) {
                    values[i] = resolver.resolver(parameter.getName(), type, message);
                } else {
                    values[i] = JSON.toJavaObject((AppJsonMessageBody)message.getBody(), type);
                }
            }
        }

        Object o = ReflectionUtils.invokeMethod(handle.getMethod(), handle.getTarget(), values);
        if(o instanceof ClientRelation) {
            return (ClientRelation) o;
        }

        return null;

    }

    @Override
    public ParamResolver getResolver(Class<? extends ParamResolver> resolver) {
        return resolverMap.get(resolver);
    }
}
