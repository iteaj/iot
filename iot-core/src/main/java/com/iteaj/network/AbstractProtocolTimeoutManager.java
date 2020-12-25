package com.iteaj.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 协议请求的超时管理, 主要功能用来做请求的同步和异步回调
 * 放入到此管理器的协议, 在请求返回的时候可以通过此管理获取原先的协议对象
 */
public abstract class AbstractProtocolTimeoutManager implements InitializingBean, DisposableBean {

    //延迟多久执行一次
    private long totalSize = 0;
    private Executor executor;
    private List<ProtocolTimeoutStorage> timeoutStorages;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String REMOVE_TOTAL_DESC = "协议超时移除 - 移除数量: {}";
    private static final String REMOVE_DESC = "协议超时({}s)移除({}) 设备编号: {} - messageId: {} - 协议类型: {} - 协议描述: {}";

    public AbstractProtocolTimeoutManager(List<ProtocolTimeoutStorage> timeoutStorages) {
        this(timeoutStorages, Executors.newFixedThreadPool(1));
    }

    public AbstractProtocolTimeoutManager(List<ProtocolTimeoutStorage> timeoutStorages, Executor executor) {
        this.executor = executor;
        this.timeoutStorages = timeoutStorages;
    }

    public void afterPropertiesSet() {
        if(CollectionUtils.isEmpty(timeoutStorages)) {
            return;
        }

        executor.execute(new TimeoutTask());
    }

    @Override
    public void destroy() throws Exception {
        if(executor instanceof ExecutorService) {
            if(!((ExecutorService) executor).isShutdown()) {
                ((ExecutorService) executor).shutdown();
            }
        }
    }

    /**
     *
     * @param protocol 要移除的协议
     * @return 协议对应的设备编号
     */
    protected abstract String protocolRemoveHandle(Protocol protocol);

    /**
     * 超时任务
     */
    class TimeoutTask implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    AbstractProtocolTimeoutManager.this.totalSize = 0;
                    for(int i=0; i < timeoutStorages.size(); i++) {
                        protocolTimeoutValidate(timeoutStorages.get(i));
                    }
                    if(AbstractProtocolTimeoutManager.this.totalSize < 1000) {
                        Thread.sleep(1500);
                    } else if(AbstractProtocolTimeoutManager.this.totalSize < 5000) {
                        Thread.sleep(800);
                    } else if(AbstractProtocolTimeoutManager.this.totalSize < 10000) {
                        Thread.sleep(300);
                    }

                    if(logger.isTraceEnabled()) {
                        logger.trace("协议超时管理 待校验协议数量({})", AbstractProtocolTimeoutManager.this.totalSize);
                    }
                }
            } catch (Exception e) {
                logger.error("协议超时管理 超时校验异常: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 协议超时校验
     * @param protocolTimeoutStorage
     */
    protected void protocolTimeoutValidate(ProtocolTimeoutStorage protocolTimeoutStorage) {
        int count = 0; //超时移除的个数

        Iterator<Map.Entry<String, Protocol>> iterator = protocolTimeoutStorage.getMapper().entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Protocol> next = iterator.next();
            ProtocolWrap protocol = (ProtocolWrap)next.getValue();
            if((System.currentTimeMillis() - protocol.getStartTime())
                    > TimeUnit.SECONDS.toMillis(protocol.getTimeout())){

                iterator.remove(); count++;

                String equipCode = protocolRemoveHandle(protocol.getProtocol());
                logger.warn(REMOVE_DESC, protocol.getTimeout(), protocolTimeoutStorage.getDesc()
                        , equipCode, next.getKey(), protocol.protocolType(), protocol.desc());
            }
        }

        AbstractProtocolTimeoutManager.this.totalSize += protocolTimeoutStorage.size();
    }

    public List<ProtocolTimeoutStorage> getTimeoutStorages() {
        return timeoutStorages;
    }

    public void setTimeoutStorages(List<ProtocolTimeoutStorage> timeoutStorages) {
        this.timeoutStorages = timeoutStorages;
    }
}
