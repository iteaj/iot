package com.iteaj.network.device.server;

import com.iteaj.network.IotServeBootstrap;
import com.iteaj.network.device.client.DeviceClientProperties;
import com.iteaj.network.device.server.env.EnvServerComponent;
import com.iteaj.network.device.server.env.m702.EnvM702ServerComponent;
import com.iteaj.network.device.server.gps.GpsServerComponent;
import com.iteaj.network.device.server.ths.ThsServerComponent;
import com.iteaj.network.device.server.pdu.PduServerComponent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass(name = "com.iteaj.network.IotServeBootstrap")
@EnableConfigurationProperties({DeviceServerProperties.class})
public class DeviceServerConfiguration {

    /**
     * 智慧融合控制台服务组件
     * @return
     */
    @Bean
    @ConditionalOnExpression("!${iot.device.server.pdu.port:'0'}.equals('0')")
    public PduServerComponent pduServerComponent() {
        return new PduServerComponent();
    }

    /**
     * 空调温湿度烟雾传感器服务组件
     * @return
     */
    @Bean
    @ConditionalOnExpression("!${iot.device.server.ths.port:'0'}.equals('0')")
    public ThsServerComponent thsServerComponent() {
        return new ThsServerComponent();
    }

    /**
     * 多功能环境监测服务组件
     * @return
     */
    @Bean
    @ConditionalOnExpression("!${iot.device.server.env.port:'0'}.equals('0')")
    public EnvServerComponent envServerComponent() {
        return new EnvServerComponent();
    }

    /**
     * 7合一环境监测服务组件
     * @return
     */
    @Bean
    @ConditionalOnExpression("!${iot.device.server.m702.port:'0'}.equals('0')")
    public EnvM702ServerComponent envM702ServerComponent() {
        return new EnvM702ServerComponent();
    }

    /**
     * Gps定位设备
     * <h4>道路运输车辆主动安全智能防控系统</h4>
     * @return
     */
    @Bean
    @ConditionalOnExpression("!${iot.device.server.gps.port:'0'}.equals('0')")
    public GpsServerComponent gpsServerComponent() {
        return new GpsServerComponent();
    }
}
