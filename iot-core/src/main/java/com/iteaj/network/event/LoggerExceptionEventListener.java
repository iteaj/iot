package com.iteaj.network.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerExceptionEventListener implements ExceptionEventListener {

    private Logger logger = LoggerFactory.getLogger(LoggerExceptionEventListener.class);

    @Override
    public void onApplicationEvent(ExceptionEvent event) {
        Throwable source = event.getSource();
        logger.error("监听异常处理 异常消息: {} - 设备编号: {}", source.getMessage(), event.getDeviceSn(), source);
    }
}
