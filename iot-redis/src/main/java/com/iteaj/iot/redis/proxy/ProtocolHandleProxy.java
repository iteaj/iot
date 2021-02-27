package com.iteaj.iot.redis.proxy;

import com.iteaj.iot.redis.ComplexRedisProducer;
import com.iteaj.iot.redis.SimpleRedisProducer;
import com.iteaj.iot.redis.producer.RedisProducer;
import com.iteaj.network.ProtocolException;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;

public class ProtocolHandleProxy {

    private static String BUSINESS_NAME = "business";

    public Object createProxy(RedisProducer target) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        try {

            proxyFactory.addAdvice((MethodInterceptor) invocation -> {
                Method method = invocation.getMethod();
                if(BUSINESS_NAME.equals(method.getName())) {
                    Object returnValue = method.invoke(invocation.getThis(), invocation.getArguments());

                    if(target instanceof SimpleRedisProducer) {

                        // 没有返回值, 直接返回
                        if(null == returnValue) {
                            return null;
                        }

                        target.persistence(returnValue);
                    } else if(target instanceof ComplexRedisProducer) {

                    }

                    return returnValue;
                }

                return method.invoke(invocation.getThis(), invocation.getArguments());
            });
        } catch (Exception e) {
            throw new ProtocolException("ProtocolHandle.business(protocol) 方法代理失败", e);
        }

        proxyFactory.setProxyTargetClass(true);
        return proxyFactory.getProxy(target.getClass().getClassLoader());
    }
}
