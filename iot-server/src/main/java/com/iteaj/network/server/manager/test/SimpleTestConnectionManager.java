package com.iteaj.network.server.manager.test;

import io.netty.channel.ChannelPipeline;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Create Date By 2017-09-13
 *
 * @author iteaj
 * @since 1.7
 */
@Component
public class SimpleTestConnectionManager extends AbstractTestManager<Object, ChannelPipeline>
        implements InitializingBean {

    private static final String TEST_DESC = "当前连接数：{} - 套接字：{} - 动作：{} - 总内存：{} - 最大内存：{} - 可用内存：{}"; //测试描述

    @Override
    public ChannelPipeline get(Object key) {
        return super.get(key);
    }

    @Override
    public ChannelPipeline add(Object key, ChannelPipeline val) {
        int count = size();
        //显示JVM总内存
        double totalMem = Runtime.getRuntime().totalMemory()/1000000.0;
        //显示JVM尝试使用的最大内存
        double maxMem = Runtime.getRuntime().maxMemory()/1000000.0;
        //空闲内存
        double freeMem = Runtime.getRuntime().freeMemory()/1000000.0;
        logger.info(TEST_DESC, count+1, key, "绑定", totalMem, maxMem, freeMem);
        return super.add(key, val);
    }

    @Override
    public ChannelPipeline remove(Object key) {
        int count = size();
        //显示JVM总内存
        double totalMem = Runtime.getRuntime().totalMemory()/1000000.0;
        //显示JVM尝试使用的最大内存
        double maxMem = Runtime.getRuntime().maxMemory()/1000000.0;
        //空闲内存
        double freeMem = Runtime.getRuntime().freeMemory()/1000000.0;
        logger.info(TEST_DESC, count-1, key, "解绑",totalMem, maxMem, freeMem);
        return super.remove(key);
    }

    @Override
    public ConcurrentHashMap getStorage() {
        return super.getStorage();
    }

    @Override
    public ConcurrentHashMap getMapper() {
        return super.getMapper();
    }

    @Override
    public void setMapper(ConcurrentHashMap mapper) {
        super.setMapper(mapper);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setMapper(new ConcurrentHashMap());
    }
}
