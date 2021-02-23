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
     * 监听客户端连接的服务端端口
     */
    private int clientPort = 8088;

    /**
     * 应用程序客户端配置信息
     */
    private AppProperties app;

    /**
     * netty Selector boss组线程数量
     */
    private short bossThreadNum = 2;

    /**
     * netty 工作组线程数量
     */
    private short workerThreadNum = 5;

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

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

        public AppProperties() {
            super(8088);
        }
    }
}
