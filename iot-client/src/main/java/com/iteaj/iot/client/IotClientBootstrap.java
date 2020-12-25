package com.iteaj.iot.client;

import com.iteaj.iot.client.http.HttpManager;
import com.iteaj.iot.client.http.okhttp.OkHttpManager;
import com.iteaj.iot.client.json.AppClientComponent;
import com.iteaj.network.DeviceManager;
import com.iteaj.network.ProtocolTimeoutStorage;
import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.client.ClientMessage;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(ClientProperties.class)
public class IotClientBootstrap implements InitializingBean, ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    public static BusinessFactory businessFactory;
    public static ApplicationContext applicationContext;
    public static ClientComponentFactory clientComponentFactory;

    @Autowired
    private ClientProperties properties;
    public static HttpManager httpManager;
    public static DeviceManager deviceManager;
    public static NioEventLoopGroup clientGroup;
    public static ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        /**
         * 只有在将客户端和服务端在同一个应用上下文的时候才会有设备管理
         */
        if(applicationContext.containsBean("deviceManager")) {
            IotClientBootstrap.deviceManager = applicationContext.getBean(DeviceManager.class);
        }

        clientComponentFactory.getComponents().forEach(item -> item.nettyClient().doConnect());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        httpManager = applicationContext.getBean(HttpManager.class);
        taskExecutor = applicationContext.getBean(ThreadPoolTaskExecutor.class);
        clientComponentFactory = applicationContext.getBean(ClientComponentFactory.class);

        if(!CollectionUtils.isEmpty(clientComponentFactory.getComponents())) {
            clientGroup = new NioEventLoopGroup(properties.getThreadNum());
            clientComponentFactory.getComponents().forEach(item -> item.nettyClient().init(clientGroup));
        }
    }

    public static <T extends IotNettyClient> T getClient(Class<? extends ClientMessage> clazz) {
        return (T) clientComponentFactory.getByClass(clazz).nettyClient();
    }

    public static <T> T getBean(Class<T> requiredClass) {
        return applicationContext.getBean(requiredClass);
    }

    @Bean("httpManager")
    @ConditionalOnMissingBean(HttpManager.class)
    public HttpManager httpManager() {
        return new OkHttpManager();
    }

    /**
     * 返回客户端组件工厂
     * @return
     */
    public static ClientComponentFactory getClientComponentFactory() {
        return clientComponentFactory;
    }

    public static ProtocolTimeoutStorage getTimeoutStorage(Class<? extends ClientMessage> clazz) {
        return clientComponentFactory.getByClass(clazz).protocolTimeoutStorage();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        IotClientBootstrap.applicationContext = applicationContext;
    }

    @Bean
    public ClientProtocolTimeoutManager clientProtocolTimeoutManager(ClientComponentFactory factory) {
        List<ProtocolTimeoutStorage> timeoutStorages = factory.getComponents().stream()
                .map(item -> item.protocolTimeoutStorage()).collect(Collectors.toList());

        return new ClientProtocolTimeoutManager(timeoutStorages);
    }

    @Bean
    public ClientProtocolHandleFactory clientBusinessFactory() {
        IotClientBootstrap.businessFactory = new ClientProtocolHandleFactory();
        return (ClientProtocolHandleFactory) IotClientBootstrap.businessFactory;
    }

    @Bean
    public ClientComponentFactory clientComponentFactory() {
        return new ClientComponentFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("!${iot.client.app.port:'false'}.equals('false')")
    public AppClientComponent appClientComponent() {
        return new AppClientComponent();
    }

}
