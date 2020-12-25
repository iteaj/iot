package com.iteaj.network.server.manager.test;

import com.iteaj.network.ConcurrentStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create Date By 2017-09-13
 *
 * @author iteaj
 * @since 1.7
 */
public abstract class AbstractTestManager<T, V> extends
        ConcurrentStorageManager<T, V> {

    private boolean start; //是否开启测试
    public Logger logger = LoggerFactory.getLogger(getClass());


    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
