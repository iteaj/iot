package com.iteaj.network.client;

import com.iteaj.network.AbstractProtocol;
import com.iteaj.network.CoreConst;
import com.iteaj.network.client.app.AppClientMessage;
import com.iteaj.network.client.app.AppClientType;
import com.iteaj.network.client.app.AppClientUtil;
import com.iteaj.network.config.DeviceProperties;
import com.iteaj.network.consts.ExecStatus;
import com.iteaj.network.server.component.DelimiterBasedFrameDecoderComponentAdapter;
import com.iteaj.network.server.component.LengthFieldBasedFrameDecoderComponentAdapter;
import com.iteaj.network.server.protocol.DeviceRequestProtocol;
import com.iteaj.network.server.protocol.NoneDealProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

public class AppClientServerComponent extends LengthFieldBasedFrameDecoderComponentAdapter<AppClientMessage> {


    public AppClientServerComponent(DeviceProperties deviceProperties) {
        super(deviceProperties, 1024 * 2048, 0, 4, 0, 4);
    }

    @Override
    public String name() {
        return "应用客户端(Netty)";
    }

    @Override
    public AbstractProtocol getProtocol(AppClientMessage message) {
        if(message.getClientType() == AppClientType.App_Client_Heart) {
            return NoneDealProtocol.getInstance(message.getHead().getEquipCode());
        } else {
            try {
                DeviceRequestProtocol requestProtocol = new AppClientServerProtocol(message).buildRequestMessage();
                /**
                 * 此处设置执行状态为 null, 为了判断是否需要直接响应客户端还是等待拿到设备报文之后在响应客户端
                 * @see AppClientServerProtocol#doBuildResponseMessage()
                 */
                requestProtocol.setExecStatus(null);
                return requestProtocol;
            } catch (Exception e) {
                DeviceRequestProtocol requestMessage = new AppClientServerProtocol(message).setFailEx(e).buildRequestMessage();
                requestMessage.setExecStatus(ExecStatus.失败); // 设置执行状态未失败, 将直接响应失败状态给客户端

                // 构建响应报文
                return requestMessage;
            }
        }
    }

    @Override
    public String desc() {
        return "用于监听应用客户端连接请求";
    }

    @Override
    public AppClientMessage decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress)ctx.channel().remoteAddress();
        String equipCode = socketAddress.getHostName()+":"+socketAddress.getPort();

        byte[] message = new byte[in.readableBytes()];
        in.readBytes(message);

        return AppClientUtil.buildClientRequestMessage(message, equipCode);
    }


}
