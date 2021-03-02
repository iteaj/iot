package com.iteaj.network.server;

import com.iteaj.network.config.DeviceProperties;
import io.netty.handler.logging.LogLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static io.netty.handler.logging.LogLevel.WARN;

@ConfigurationProperties(prefix = "iot.server")
public class IotServerProperties {

    /**
     * netty 打印的日志级别
     */
    private LogLevel level = WARN;

    /**
     * 应用程序客户端配置信息, 默认启用8088端口
     */
    private AppProperties app = new AppProperties();

    /**
     * netty Selector boss组线程数量
     */
    private short bossThreadNum = 2;

    /**
     * netty 工作组线程数量
     */
    private short workerThreadNum = 5;

    public short getBossThreadNum() {
        return bossThreadNum;
    }

    public void setBossThreadNum(short bossThreadNum) {
        this.bossThreadNum = bossThreadNum;
    }

    public short getWorkerThreadNum() {
        return workerThreadNum;
    }

    public void setWorkerThreadNum(short workerThreadNum) {
        this.workerThreadNum = workerThreadNum;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public AppProperties getApp() {
        return app;
    }

    public IotServerProperties setApp(AppProperties app) {
        this.app = app;
        return this;
    }

    public static class AppProperties extends DeviceProperties {

        /**
         * 是否启用客户端端口监听
         */
        private boolean start = true;

        public AppProperties() {
            super(8088);
        }

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }
}
