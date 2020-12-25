package com.iteaj.network.device.client;

import com.iteaj.network.config.DeviceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iot.device.client")
public class DeviceClientProperties {

    /**
     * 福州断路器配置
     */
    private ClientConfig fzb;

    /**
     * 空调温控面板配置
     */
    private ClientConfig lrm;

    public ClientConfig getFzb() {
        return fzb;
    }

    public void setFzb(ClientConfig fzb) {
        this.fzb = fzb;
    }

    public ClientConfig getLrm() {
        return lrm;
    }

    public void setLrm(ClientConfig lrm) {
        this.lrm = lrm;
    }

    public static class ClientConfig extends DeviceProperties {

        /**
         * 是否启用设备
         */
        private boolean start;

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }
    }
}
