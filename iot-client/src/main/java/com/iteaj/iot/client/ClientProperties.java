package com.iteaj.iot.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iot.client")
public class ClientProperties {

    /**
     * netty 线程组数量
     */
    private short threadNum = 5;

    /**
     * 应用程序客户端配置
     */
    private AppClient app;

    /**
     * MQTT Broker服务配置
     */
    private MqttClientConfig mqtt;

    public static class AppClient {

        /**
         * 服务端主机端口
         */
        private short port;
        /**
         * 服务端主机地址
         */
        private String host;

        public short getPort() {
            return port;
        }

        public void setPort(short port) {
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    public static class MqttClientConfig {

        /**
         * Broker主机端口
         */
        private short port;
        /**
         * Broker主机地址
         */
        private String host;

        public short getPort() {
            return port;
        }

        public void setPort(short port) {
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    public AppClient getApp() {
        return app;
    }

    public void setApp(AppClient app) {
        this.app = app;
    }

    public MqttClientConfig getMqtt() {
        return mqtt;
    }

    public void setMqtt(MqttClientConfig mqtt) {
        this.mqtt = mqtt;
    }

    public short getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(short threadNum) {
        this.threadNum = threadNum;
    }
}
