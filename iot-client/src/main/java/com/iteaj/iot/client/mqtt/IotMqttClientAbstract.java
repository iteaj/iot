package com.iteaj.iot.client.mqtt;

import com.iteaj.iot.client.ClientComponent;
import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.handle.MqttClientHandler;
import com.iteaj.iot.client.mqtt.api.MqttConsumerListener;
import com.iteaj.iot.client.mqtt.api.MqttConsumerProcess;
import com.iteaj.iot.client.mqtt.api.MqttProducerProcess;
import com.iteaj.iot.client.mqtt.api.impl.MqttConsumerProcessImpl;
import com.iteaj.iot.client.mqtt.api.impl.MqttProducerProcessImpl;
import com.iteaj.iot.client.mqtt.common.*;
import com.iteaj.iot.client.mqtt.common.exception.LoginException;
import com.iteaj.iot.client.mqtt.protocol.ClientProtocolProcess;
import com.iteaj.iot.client.mqtt.protocol.ClientProtocolUtil;
import com.iteaj.network.utils.StringsUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class IotMqttClientAbstract extends IotNettyClient implements ClientProcess {

    private int port;
    private String host;

    protected MqttConnectOptions info;
    protected MqttProducerProcess producerProcess;
    protected MqttConsumerProcess consumerProcess;
    private List<MqttConsumerListener> listenerList;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public IotMqttClientAbstract(int port, String host, List<MqttConsumerListener> listenerList, ClientComponent clientComponent) {
        super(clientComponent);
        this.port = port;
        this.host = host;
        this.listenerList = listenerList;
    }

    @Override
    public void init(NioEventLoopGroup clientGroup) {
        this.info = getConnectOptions();
        if(this.info.getClientIdentifier() == null) {
            throw new MqttClientException("未设置客户端id");
        }

        this.producerProcess = new MqttProducerProcessImpl(this);
        this.consumerProcess = new MqttConsumerProcessImpl(this);
        this.consumerProcess.setConsumerListener(listenerList);

        super.init(clientGroup);
    }

    /**
     * 连接成功回调
     * @param future
     */
    @Override
    public void successCallback(ChannelFuture future) {
        getChannel().writeAndFlush(ClientProtocolUtil.connectMessage(info));
        logger.debug("Mqtt({})发起连接请求 请求参数: {} ", this.getName(), info.toString());
    }

    protected MqttConnectOptions getConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setHasCleanSession(true);
        mqttConnectOptions.setClientIdentifier(this.getClientId());
        return mqttConnectOptions;
    }

    /**
     * 返回Mqtt客户端Id
     * @return
     */
    protected abstract String getClientId();

    @Override
    protected Bootstrap doInit(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30 * 1000);

        return bootstrap;
    }

    @Override
    protected void doInitChannel(SocketChannel ch) {
        ClientProtocolProcess proObj = new ClientProtocolProcess(this, consumerProcess, producerProcess);

        ch.pipeline().addFirst("decoder", new MqttDecoder());
        ch.pipeline().addFirst("encoder", MqttEncoder.INSTANCE);
        ch.pipeline().addFirst(new IdleStateHandler(30, 60, 120));

        ch.pipeline().addLast("mqttHandler", new MqttClientHandler(proObj, getClientComponent()));
    }

    @Override
    protected ChannelInboundHandler initClientRequestDecoder() {
        return null;
    }

    @Override
    public void loginFinish(boolean bResult, LoginException exception) {
        if (bResult) {
            logger.info("Mqtt({})登录成功", getName());
            NettyUtil.setClientId(getChannel(), info.getClientIdentifier());

            this.subscribeTopic(consumerProcess);


        } else {
            if (exception != null) {
                logger.error("login error", exception);
            }
        }
    }

    /**
     * 订阅主题
     * @param process
     */
    protected abstract void subscribeTopic(MqttConsumerProcess process);

    @Override
    public void disConnect() {
        if (getChannel() != null) {
            logger.warn("Mqtt客户端({}) 发布断开连接");
            getChannel().writeAndFlush(ClientProtocolUtil.disConnectMessage());
        }
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getHost() {
        return host;
    }

}
