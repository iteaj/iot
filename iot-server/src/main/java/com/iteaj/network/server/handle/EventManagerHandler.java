package com.iteaj.network.server.handle;

import com.iteaj.network.CoreConst;
import com.iteaj.network.DeviceManager;
import com.iteaj.network.IotServeBootstrap;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.event.DeviceEvent;
import com.iteaj.network.event.DeviceEventType;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.ServerComponent;
import com.iteaj.network.server.ServerComponentFactory;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * <p>事件管理处理器</p>
 * 用来管理平台上的各类事件
 * Create Date By 2017-09-08
 * @author iteaj
 * @since 1.7
 */
@ChannelHandler.Sharable
public class EventManagerHandler extends SimpleChannelInboundHandler<UnParseBodyMessage> {

    private static DeviceManager deviceManager; //设备管理器
    private static EventManagerHandler managerHandler;
    private ServerComponentFactory componentFactory;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static EventManagerHandler getInstance(DeviceManager deviceManager, ServerComponentFactory componentFactory) {
        if(managerHandler == null) {
            managerHandler = new EventManagerHandler(deviceManager, componentFactory);
        }
        return managerHandler;
    }

    protected EventManagerHandler(DeviceManager deviceManager, ServerComponentFactory componentFactory) {
        this.deviceManager = deviceManager;
        this.componentFactory = componentFactory;
    }

    /**
     * 处理心跳事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){ //心跳事件
            IdleState state = ((IdleStateEvent) evt).state();
            switch (state){
                case READER_IDLE:
                    readerHandle(ctx, "读超时");
                    break;
                case WRITER_IDLE:
                    readerHandle(ctx, "写超时");
                    break;
                case ALL_IDLE:
                    readerHandle(ctx, "读写超时");
                    break;
            }

        }
        //设备心跳事件在此处理,不触发下一个
//        super.userEventTriggered(ctx, evt);
    }

    /**
     * 读
     * @param ctx
     */
    protected void readerHandle(final ChannelHandlerContext ctx, String desc){

        String equipCode = (String) ctx.channel().attr(CoreConst.EQUIP_CODE).get();
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        final String target = address.getHostString()+":"+address.getPort();

        ctx.channel().close().addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()){
                //链接关闭的时候移除对应的设备
                if(StringUtils.isNotBlank(equipCode)) {
                    deviceManager.remove(equipCode);
                    logger.warn("{} - 设备编号: {} - 客户端地址: {} - 说明: 主动下线设备(下线成功)", desc, equipCode, target);
                }

            } else {
                logger.warn("{} - 设备编号: {} - 客户端地址: {} - 说明: 主动下线设备(下线失败)", desc, equipCode, target);
            }
        });

        return;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnParseBodyMessage msg) throws Exception {
        if(msg.getHead() == null || msg.getHead().getEquipCode() == null) {
            logger.error("事件管理器错误, 没办法注册设备到设备管理器, 请检查报文是否包含报文头及报文头包含设备编号 - 报文: {}", msg);
            return;
        }

        //获取设备编号
        String equipCode = (String) ctx.channel().attr(CoreConst.EQUIP_CODE).get();
        if(null == equipCode) {
            equipCode = msg.getHead().getEquipCode();

            // 设置设备编号到对应的Channel
            ctx.channel().attr(CoreConst.EQUIP_CODE).setIfAbsent(equipCode);
        }

        ChannelPipeline pipeline = (ChannelPipeline) deviceManager.get(equipCode);
        //设备还没有注册到设备管理器,则注册
        if(null == pipeline){
            deviceManager.add(equipCode, ctx.pipeline());

            //触发设备上线事件
            IotServeBootstrap.publishApplicationEvent(new DeviceEvent(equipCode, DeviceEventType.设备上线));

            if(logger.isDebugEnabled()) {
                ServerComponent serverComponent = componentFactory.getByClass(msg.getClass());
                logger.debug("设备上线({}) - 设备编号: {} - 服务描述: {}", serverComponent.name()
                        , equipCode, serverComponent.deviceServer().desc());
            }
        } else { //设备已经存在,判断是否是同一台设备

            final String deviceSn = equipCode;
            //不是同一台设备则关闭早期一台
            if(pipeline != ctx.pipeline()){
                pipeline.close().addListener((ChannelFutureListener) future -> {
                    String status = future.isSuccess() ? "成功" : "失败";
                    logger.warn("设备编号冲突 - 设备编号: {} - 处理方案: 移除早期的一台 - 关闭状态：{}", deviceSn, status);
                });

                // 先移除早期的一台
                deviceManager.remove(equipCode);
                // 新增最新的设备
                deviceManager.add(equipCode, ctx.pipeline());

                //触发设备上线事件
                IotServeBootstrap.publishApplicationEvent(new DeviceEvent(equipCode, DeviceEventType.设备上线));
            }
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            Attribute attribute = ctx.channel().attr(CoreConst.EQUIP_CODE);
            InetSocketAddress address = (InetSocketAddress)ctx.channel().localAddress();
            ServerComponent serverComponent = componentFactory.getByPort(address.getPort());

            if(attribute != null) {
                Object equipCode = attribute.get();
                if(equipCode instanceof String) {

                    // 断线后移除设备
                    Object remove = deviceManager.remove(equipCode);

                    //触发设备下线事件
                    InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                    IotServeBootstrap.publishApplicationEvent(new DeviceEvent((String) equipCode, DeviceEventType.设备断线));

                    logger.warn("设备断线({}) - 设备编号: {} - 客户端地址: {} - 状态：{} - 说明: 移除从设备管理里面"
                            , serverComponent.name(), equipCode, remoteAddress.getHostName()+":"+remoteAddress.getPort()
                            , remove != null ? "移除成功" : "移除失败");

                    return;
                }
            }

            logger.error("设备断线({}) 移除设备失败", serverComponent.name()
                    , new ProtocolException("获取不到设备编号[io.netty.util.Attribute.get() == null]" +
                            ", 请确认是否保存设备编号[io.netty.util.Attribute.set()], 具体属性[CoreConst.EQUIP_CODE]"));
        } finally {
            ctx.fireChannelInactive();
        }

    }

}
