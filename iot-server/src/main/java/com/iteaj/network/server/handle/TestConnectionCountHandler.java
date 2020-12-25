package com.iteaj.network.server.handle;

import com.iteaj.network.server.manager.test.AbstractTestManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

/**
 * Create Date By 2017-09-13
 *
 * @author iteaj
 * @since 1.7
 */
public class TestConnectionCountHandler extends SimpleChannelInboundHandler<String> {

    public AbstractTestManager manager;

    public TestConnectionCountHandler(AbstractTestManager manager) {
        this.manager = manager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //如果不开启测试
        if(!manager.isStart()){
            super.channelActive(ctx);
            return;
        }
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
//        manager.logger.warn("激活设备：{}", remoteAddress.getHostName()+":"+remoteAddress.getPort());
        manager.add(remoteAddress.getHostName()+":"+remoteAddress.getPort(), ctx.pipeline());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //如果不开启测试
        if(!manager.isStart()){
            super.channelInactive(ctx);
            return;
        }
        final InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        manager.remove(remoteAddress.getHostName()+":"+remoteAddress.getPort());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        manager.logger.warn("链接注册：{}", remoteAddress.getHostName()+":"+remoteAddress.getPort());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        manager.logger.warn("链接解除注册：{}", remoteAddress.getHostName()+":"+remoteAddress.getPort());
        super.channelUnregistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //如果不开启测试
        if(!manager.isStart()){
            return;
        }
        System.out.println("有测试数据接入："+msg);
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        byte[] bytes =(remoteAddress.getHostString()+":"+remoteAddress.getPort()+"--"+msg).getBytes();
        ByteBuf byteBuf = ctx.alloc().heapBuffer(bytes.length).writeBytes(bytes);
        ctx.writeAndFlush(byteBuf);
    }
}
