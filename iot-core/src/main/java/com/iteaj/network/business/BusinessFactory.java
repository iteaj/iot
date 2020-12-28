package com.iteaj.network.business;

import com.iteaj.network.Protocol;
import com.iteaj.network.ProtocolHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>业务工厂</p>
 * 通过协议类型获取对应的协议业务
 * Create Date By 2020-09-21
 * @author iteaj
 * @since 1.8
 */
public abstract class BusinessFactory<T extends ProtocolHandle> implements InitializingBean, BeanFactoryAware {

    private ListableBeanFactory beanFactory;
    private HashMap<Class<? extends Protocol>, T> mapper;
    private Logger logger = LoggerFactory.getLogger(BusinessFactory.class);

    /**
     * 通过协议类型获取对应的协议业务
     * @param protocolClazz
     * @return
     */
    public T getProtocolHandle(Class<? extends Protocol> protocolClazz){
        return mapper.get(protocolClazz);
    }

    public void register(Class<? extends Protocol> type, T business) {
        if (null == type) {
            logger.error("注册协议处理器失败 请指定对应的协议类型(泛型)<T> - 业务类型：{}<T> - 说明: 必须指定泛型T", business.getClass().getSimpleName());
            throw new BeanInitializationException("未指定协议业务对应的协议类型<T>, 必须指定泛型T");
        }

        // 协议类型不能是抽象类和接口
        int modifiers = type.getModifiers();
        if(Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
            logger.error("注册协议处理器失败 错误的协议类型(不能是抽象类和接口)[{}] - 业务类型：{}<T> - 说明: 业务对象只能注册到具体的实现协议, 必须指定泛型T "
                    , type.getSimpleName(), business.getClass().getSimpleName());
            throw new BeanInitializationException("错误的协议类型(不能是抽象类和接口)["+type.getName()+"]");
        }

        if(mapper.containsKey(type)) {
            T t = mapper.get(type);
            logger.error("注册协议处理器失败 此协议[{}]已经包含了业务[{}] - 说明: 一个协议只能对应一个协议业务对象", type.getName(), t.getClass().getName());
            throw new BeanInitializationException("此协议["+type.getName()+"]已经包含了业务["+t.getClass().getName()+"], 一个协议只能对应一个协议业务对象");
        }

        mapper.put(type, business);
        logger.info("注册协议处理器 协议类型[{}] -> 业务类型[{}<{}>]", type.getSimpleName(), business.getClass().getSimpleName(), type.getSimpleName());
    }

    @Override
    public void afterPropertiesSet() {
        mapper = new HashMap<>();

        //获取所有的协议业务的对象
        Map<String, T> beansOfType = beanFactory.getBeansOfType(getServiceClass());

        //注册协议对象到业务4工厂
        if(CollectionUtils.isEmpty(beansOfType)) {
            logger.warn("注册协议业务 - 没有业务对象被注册：{}" + ProtocolHandle.class.getName());
        } else {
            for (T item : beansOfType.values()) {
                Class<? extends Protocol> type = getProtocolClass(item);

                this.register(type, item);
            }
        }
    }

    protected abstract Class<? extends Protocol> getProtocolClass(T item);

    protected abstract Class<T> getServiceClass();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }
}
