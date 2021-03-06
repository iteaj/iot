package com.iteaj.network.client.handle;

import java.lang.reflect.Method;

/**
 * create time: 2021/3/4
 *
 * @author iteaj
 * @since 1.0
 */
public class MethodMeta {

    private Method method;
    private Object target;
    private IotCtrl mapping;
    private IotCtrl handle;

    public MethodMeta(Method method, Object target, IotCtrl mapping, IotCtrl handle) {
        this.method = method;
        this.target = target;
        this.handle = handle;
        this.mapping = mapping;
    }

    public Class getResolverClazz() {
        return this.handle.resolver();
    }

    public Method getMethod() {
        return method;
    }

    public MethodMeta setMethod(Method method) {
        this.method = method;
        return this;
    }

    public IotCtrl getMapping() {
        return mapping;
    }

    public MethodMeta setMapping(IotCtrl mapping) {
        this.mapping = mapping;
        return this;
    }

    public Object getTarget() {
        return target;
    }

    public MethodMeta setTarget(Object target) {
        this.target = target;
        return this;
    }
}
