package com.iteaj.network.device.client;

import com.iteaj.iot.client.IotClientBootstrap;
import com.iteaj.network.device.client.breaker.fzwu.BreakerClientComponent;
import com.iteaj.network.device.client.ksd.lrmo.KLrmClientComponent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass(name = "com.iteaj.iot.client.IotClientBootstrap")
@EnableConfigurationProperties(DeviceClientProperties.class)
public class DeviceClientConfiguration {

    /**
     * 福州吴总生产的多功能断路器设备
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "iot.device.client", value = "fzb.start", havingValue = "true")
    public BreakerClientComponent breakerClientComponent() {
        return new BreakerClientComponent();
    }

    /**
     *  空调温控面板设备(Lora网关 + Modbus协议)
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "iot.device.client", value = "lrm.start", havingValue = "true")
    public KLrmClientComponent kLrmClientComponent() {
        return new KLrmClientComponent();
    }
}
