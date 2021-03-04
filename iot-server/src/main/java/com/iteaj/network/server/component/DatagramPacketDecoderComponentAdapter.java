package com.iteaj.network.server.component;

import com.iteaj.network.AbstractMessage;
import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.DeviceProtocolFactory;
import com.iteaj.network.Protocol;
import com.iteaj.network.codec.DeviceMessageDecoder;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.AbstractUdpServer;
import com.iteaj.network.server.DeviceUdpServerComponent;
import com.iteaj.network.server.IotDeviceServer;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.core.GenericTypeResolver;

/**
 * 适配Udp DatagramPacket解码组件
 * @param <M>
 */
public abstract class DatagramPacketDecoderComponentAdapter<M extends UnParseBodyMessage> extends DeviceUdpServerComponent
        implements IotDeviceServer, DeviceProtocolFactory<M>, DeviceMessageDecoder<M, DatagramPacket> {

    private int port;
    private UdpServerWrapper udpServerWrapper;
    private ProtocolFactoryComponentWrapper delegation;
    private DatagramPacketSimpleHandle datagramPacketSimpleHandle;

    public DatagramPacketDecoderComponentAdapter(int port) {
        this.port = port;
        this.udpServerWrapper = new UdpServerWrapper(port);
        this.datagramPacketSimpleHandle = new DatagramPacketSimpleHandle();
        this.delegation = new ProtocolFactoryComponentWrapper(this);
    }

    @Override
    public abstract String name();

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

    @Override
    public int port() {
        return this.port;
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return this.datagramPacketSimpleHandle;
    }

    @Override
    protected IotDeviceServer createDeviceServer() {
        return this.udpServerWrapper;
    }

    @Override
    protected DeviceProtocolFactory createProtocolFactory() {
        return this.delegation;
    }

    @Override
    public void initChannelPipeline(ChannelPipeline pipeline) {
        udpServerWrapper.initChannelPipeline(pipeline);
    }

    @Override
    public abstract M decode(ChannelHandlerContext ctx, DatagramPacket in) throws Exception;

    @Override
    public Class<? extends AbstractMessage> messageClass() {
        Class<?> aClass = GenericTypeResolver.resolveTypeArgument(getClass()
                , DatagramPacketDecoderComponentAdapter.class);
        return (Class<M>) aClass;
    }

    protected class UdpServerWrapper extends AbstractUdpServer {

        public UdpServerWrapper(int port) {
            super(port);
        }

        @Override
        public String name() {
            return DatagramPacketDecoderComponentAdapter.this.name();
        }

        @Override
        public String desc() {
            return DatagramPacketDecoderComponentAdapter.this.desc();
        }

        @Override
        public ChannelInboundHandlerAdapter getMessageDecoder() {
            return DatagramPacketDecoderComponentAdapter.this.getMessageDecoder();
        }
    }

    @ChannelHandler.Sharable
    protected class DatagramPacketSimpleHandle extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
            final M decode = DatagramPacketDecoderComponentAdapter.this.proxy(ctx, msg);
            if(decode != null) {
                ctx.fireChannelRead(msg);
            }
        }
    }
}
