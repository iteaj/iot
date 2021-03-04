package com.iteaj.network.server.component;

import com.iteaj.network.*;
import com.iteaj.network.codec.DeviceMessageDecoder;
import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.AbstractDeviceServer;
import com.iteaj.network.server.DeviceServerComponent;
import com.iteaj.network.server.IotDeviceServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import org.springframework.core.GenericTypeResolver;

import java.util.List;

/**
 * create time: 2021/2/20
 *
 * @author iteaj
 * @since 1.0
 */
public abstract class DeviceTcpDecoderComponent<M extends UnParseBodyMessage> extends DeviceServerComponent
        implements IotDeviceServer, DeviceProtocolFactory<M>, DeviceMessageDecoder<M, ByteBuf> {

    private ProtocolFactoryComponentWrapper delegation;
    private DeviceProperties deviceProperties;
    private DeviceServerWrapper deviceServerWrapper;

    public DeviceTcpDecoderComponent(DeviceProperties deviceProperties) {
        this.deviceProperties = deviceProperties;
        this.deviceServerWrapper = new DeviceServerWrapper(deviceProperties);
        this.delegation = new ProtocolFactoryComponentWrapper(this);
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
    public abstract M decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception;

    @Override
    public Class<M> messageClass() {
        Class<?> aClass = GenericTypeResolver.resolveTypeArgument(getClass()
                , DeviceTcpDecoderComponent.class);
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
            return DeviceTcpDecoderComponent.this.getMessageDecoder();
        }

        @Override
        public String name() {
            return DeviceTcpDecoderComponent.this.name();
        }

        @Override
        public String desc() {
            return DeviceTcpDecoderComponent.this.desc();
        }
    }

}
