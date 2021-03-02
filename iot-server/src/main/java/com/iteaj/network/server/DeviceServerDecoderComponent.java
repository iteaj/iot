package com.iteaj.network.server;

import com.iteaj.network.*;
import com.iteaj.network.codec.DeviceMessageDecoder;
import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.message.UnParseBodyMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;

/**
 * create time: 2021/2/20
 *
 * @author iteaj
 * @since 1.0
 */
public abstract class DeviceServerDecoderComponent<M extends UnParseBodyMessage> extends DeviceServerComponent
        implements IotDeviceServer, DeviceProtocolFactory<M>, DeviceMessageDecoder<M> {

    private ProtocolFactoryWrapper delegation;
    private DeviceProperties deviceProperties;
    private DeviceServerWrapper deviceServerWrapper;

    public DeviceServerDecoderComponent(DeviceProperties deviceProperties) {
        this.deviceProperties = deviceProperties;
        this.delegation = new ProtocolFactoryWrapper();
        this.deviceServerWrapper = new DeviceServerWrapper(deviceProperties);
    }

    @Override
    public abstract String name();

    @Override
    public int port() {
        return this.deviceProperties.getPort();
    }

    @Override
    protected IotDeviceServer createDeviceServer() {
        return this.deviceServerWrapper;
    }

    @Override
    protected DeviceProtocolFactory createProtocolFactory() {
        return this.delegation;
    }

    @Override
    public void initChannelPipeline(ChannelPipeline pipeline) {
        deviceServerWrapper.initChannelPipeline(pipeline);
    }


    @Override
    public Class<M> messageClass() {
        Class<?> aClass = GenericTypeResolver.resolveTypeArgument(getClass()
                , DeviceServerDecoderComponent.class);
        return (Class<M>) aClass;
    }

    @Override
    public AbstractProtocol get(String key) {
        return this.delegation.get(key);
    }

    @Override
    public AbstractProtocol add(String key, Protocol val) {
        return this.delegation.add(key, val);
    }

    @Override
    public AbstractProtocol remove(String key) {
        return this.delegation.remove(key);
    }

    @Override
    public boolean isExists(String key) {
        return this.delegation.isExists(key);
    }

    @Override
    public Object getStorage() {
        return this.delegation.getStorage();
    }

    protected class DeviceServerWrapper extends AbstractDeviceServer {

        public DeviceServerWrapper(int port) {
            super(port);
        }

        public DeviceServerWrapper(DeviceProperties serverConfig) {
            super(serverConfig);
        }

        @Override
        public ChannelInboundHandlerAdapter getMessageDecoder() {
            return DeviceServerDecoderComponent.this.getMessageDecoder();
        }

        @Override
        public String name() {
            return DeviceServerDecoderComponent.this.name();
        }

        @Override
        public String desc() {
            return DeviceServerDecoderComponent.this.desc();
        }
    }

    protected class ProtocolFactoryWrapper extends ProtocolFactory<M> {

        @Override
        public AbstractProtocol getProtocol(M message) {
            return DeviceServerDecoderComponent.this.getProtocol(message);
        }
    }
}
