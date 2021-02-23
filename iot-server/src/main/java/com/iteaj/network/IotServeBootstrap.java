package com.iteaj.network;

import com.iteaj.network.business.BusinessFactory;
import com.iteaj.network.client.AppClientServerComponent;
import com.iteaj.network.client.AppClientServerHandle;
import com.iteaj.network.server.*;
import com.iteaj.network.server.codec.DeviceProtocolEncoder;
import com.iteaj.network.server.handle.EventManagerHandler;
import com.iteaj.network.server.handle.ProtocolBusinessHandler;
import com.iteaj.network.server.manager.DevicePipelineManager;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create Date By 2017-09-06
 *  <h4>设备网络通信程序启动入口</h4>
 * @author teaj
 * @since 1.7
 */

@EnableConfigurationProperties(IotServerProperties.class)
public class IotServeBootstrap implements InitializingBean,DisposableBean
        , BeanFactoryAware, ApplicationListener<ContextRefreshedEvent> {

    private boolean ssl; //是否开启ssl证书

    @Autowired
    private IotServerProperties properties;
    public static BeanFactory BEAN_FACTORY; //spring bean factory
    protected static ApplicationContext applicationContext;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    @Autowired
    private DeviceManager deviceManager;

    private Bootstrap udpBootstrap;
    private ServerBootstrap tcpBootstrap;

    @Autowired
    public DeviceProtocolEncoder protocolEncoder;

    public static ServerProtocolHandleFactory BUSINESS_FACTORY;
    private static ServerComponentFactory COMPONENT_FACTORY;

    private static Logger logger = LoggerFactory.getLogger(IotServeBootstrap.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        IotServeBootstrap.BUSINESS_FACTORY = BEAN_FACTORY.getBean(ServerProtocolHandleFactory.class);
        IotServeBootstrap.COMPONENT_FACTORY = BEAN_FACTORY.getBean(ServerComponentFactory.class);
        if(null == BUSINESS_FACTORY) {
            throw new IllegalArgumentException("找不到业务工厂：" + ServerProtocolHandleFactory.class.getName());
        }

        //初始化Netty服务
        initNettyServer();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Spring容器启动完成, 正在开启Netty服务...");
        IotServeBootstrap.applicationContext = event.getApplicationContext();

        try {
            doBind(applicationContext);
        } catch (Exception exception) {

            // 异常关闭应用上下文
            if(applicationContext instanceof ConfigurableApplicationContext) {
                ((ConfigurableApplicationContext) applicationContext).close();
            }
        }
    }

    /**
     * 开启netty服务器
     */
    protected IotServeBootstrap initNettyServer() {
        try {
            //验证tcp协议是否开启ssl
            final SslContext sslCtx;
            if (ssl) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            // Netty框架配置
            bossGroup = new NioEventLoopGroup(properties.getBossThreadNum(), new DefaultThreadFactory("IotSelector"));
            workerGroup = new NioEventLoopGroup(properties.getWorkerThreadNum(), new DefaultThreadFactory("IotWorker"));

            // 初始化tcp服务
            final List<DeviceServerComponent> serverComponents = COMPONENT_FACTORY.getServerComponents();
            if(!CollectionUtils.isEmpty(serverComponents)) {
                initTcpServe(sslCtx);
            }

        } catch (CertificateException e) {
            logger.error("Nio服务端Ssl证书异常：", e);
        } catch (SSLException e) {
            logger.error("Nio服务端Ssl异常：", e);
        } catch (Exception e) {
            logger.error("Nio服务端启动类未知异常：", e);
        }

        return this;
    }

    protected void initTcpServe(SslContext sslCtx) {
        tcpBootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(properties.getLevel()))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        if (sslCtx != null) {
                            p.addLast(sslCtx.newHandler(ch.alloc()));
                        }

                        int port = ch.localAddress().getPort();
                        IotServeBootstrap.this.doSocketChannelInitializer(ch, p, port);
                    }
                });
    }

    protected void doSocketChannelInitializer(SocketChannel ch, ChannelPipeline p, int port) {
        ServerComponent serverComponent = COMPONENT_FACTORY.getByPort(port);
        if(serverComponent instanceof DeviceServerComponent) {
            IotDeviceServer iotDeviceServer = serverComponent.deviceServer();
            if(iotDeviceServer instanceof BeanFactoryAware) {
                ((BeanFactoryAware) iotDeviceServer).setBeanFactory(BEAN_FACTORY);
            }

            iotDeviceServer.initChannelPipeline(p);

            // 新增事件管理处理
            if(p.get("idleState") != null) { // 如果有启用超时处理
                p.addAfter("idleState", "eventManager", new EventManagerHandler(deviceManager, serverComponent));
            } else {
                p.addLast("eventManager", new EventManagerHandler(deviceManager, serverComponent));
            }

            p.addFirst("encode", protocolEncoder); // 设备协议编码通用
            p.addLast("protocolHandle", new ProtocolBusinessHandler(COMPONENT_FACTORY, BUSINESS_FACTORY));
        } else {
            logger.error("查无与端口: {}匹配的服务组件: {}, 所有与此端口连接的设备都无法处理", port, DeviceServerComponent.class.getSimpleName());
        }
    }

    protected void doBind(final ApplicationContext context){
        // 监听TCP端口
        COMPONENT_FACTORY.getServerComponents().forEach(item -> item.deviceServer().doBind(tcpBootstrap, context));

        // 监听UDP端口
        COMPONENT_FACTORY.getUdpServerComponents().forEach(item -> item.deviceServer().doBind(getUdpBootstrap(item), context));
    }

    private AbstractBootstrap getUdpBootstrap(DeviceUdpServerComponent component) {
        return new Bootstrap().group(workerGroup)
                .channel(NioDatagramChannel.class)
                // 设置读缓冲区为2M
                .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                // 设置写缓冲区为1M
                .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
//                .option(EpollChannelOption.SO_REUSEPORT, true)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        component.deviceServer().initChannelPipeline(ch.pipeline());
                        ch.pipeline().addLast("protocolHandle", new ProtocolBusinessHandler(COMPONENT_FACTORY, BUSINESS_FACTORY));
                    }
                });
    }

    public static void publishApplicationEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    public static <T> T getBean(Class<T> requiredClass) {
        return BEAN_FACTORY.getBean(requiredClass);
    }

    public static ServerComponent getServerComponent(Class<? extends AbstractMessage> messageClass) {
        return COMPONENT_FACTORY.getByClass(messageClass);
    }

    public static BusinessFactory getBusinessFactory() {
        return IotServeBootstrap.BUSINESS_FACTORY;
    }

    @Bean
    public ServerProtocolHandleFactory deviceRequestBusinessFactory() {
        return new ServerProtocolHandleFactory();
    }

    @Bean
    public AppClientServerHandle appClientServerService() {
        return new AppClientServerHandle();
    }

    @Bean({"deviceManager"})
    public DevicePipelineManager devicePipelineManager() {
        return DevicePipelineManager.getInstance();
    }

    /**
     * 服务组件工厂
     * @return
     */
    @Bean
    public ServerComponentFactory serverComponentFactory() {
        return new ServerComponentFactory();
    }

    /**
     * 设备协议编码器
     * 此编码器用于所有的设备
     * @see ChannelHandler.Sharable 单例处理器
     * @see AbstractMessage#getMessage()
     * @param componentFactory
     * @return
     */
    @Bean
    public DeviceProtocolEncoder deviceProtocolEncoder(ServerComponentFactory componentFactory) {
        return new DeviceProtocolEncoder(componentFactory);
    }

    /**
     * 用于监听应用程序客户端链接的服务端组件, 默认启用
     * @return
     */
    @Bean
    @ConditionalOnExpression("!${iot.server.app.port:'0'}.equals('0')")
    public AppClientServerComponent clientServerComponent(IotServerProperties properties) {
        return new AppClientServerComponent(properties.getApp());
    }

    /**
     * 设备服务端协议的超时管理器
     * @param componentFactory
     * @return
     */
    @Bean
    public ServerTimeoutProtocolManager serverTimeoutProtocolManager(ServerComponentFactory componentFactory) {
        List<ProtocolTimeoutStorage> storages = componentFactory.getServerComponents()
                .stream()
                .filter(item->item.protocolTimeoutStorage() != null)
                .map(item -> item.protocolTimeoutStorage())
                .collect(Collectors.toList());

        return new ServerTimeoutProtocolManager(storages);
    }

    /**
     * 关闭所有端口
     * @param context
     */
    public void close(ApplicationContext context){
        try {
            if(bossGroup != null)
                bossGroup.shutdownGracefully();
            if(workerGroup != null)
                workerGroup.shutdownGracefully();

            //关闭应用上下文
            if(context instanceof ConfigurableApplicationContext){
                logger.warn("正在尝试关闭 Spring ApplicationContext");
                ((ConfigurableApplicationContext) context).close();
                logger.warn("Spring ApplicationContext成功关闭");
            }
        } catch (Exception e) {
            logger.error("关闭应用时错误", e);
        }
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        IotServeBootstrap.BEAN_FACTORY = beanFactory;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    @Override
    public void destroy() throws Exception {
        if(bossGroup != null)
            bossGroup.shutdownGracefully();
        if(workerGroup != null)
            workerGroup.shutdownGracefully();
    }
}
