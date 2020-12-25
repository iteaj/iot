package com.iteaj.network.server.handle;

import com.iteaj.network.DeviceManager;
import com.iteaj.network.IotServeBootstrap;
import com.iteaj.network.event.DeviceEvent;
import com.iteaj.network.event.DeviceEventType;
import com.iteaj.network.event.ExceptionEvent;
import com.iteaj.network.message.UnParseBodyMessage;
import com.iteaj.network.server.DeviceServerComponent;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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
public class EventManagerHandler extends SimpleChannelInboundHandler<UnParseBodyMessage> {

    private int count = 0; //计数器
    private int idleTimesToClose = 3; //多少次关闭链接
    /**用来存放当前{@link io.netty.channel.Channel}对应的设备的设备号*/
    private String equipCode;
    private static DeviceManager deviceManager; //设备管理器
    private DeviceServerComponent serverComponent; // 此事件管理器管理哪个服务端设备
    private Logger logger = LoggerFactory.getLogger(getClass());

    public EventManagerHandler(DeviceManager deviceManager, DeviceServerComponent serverComponent) {
        this.deviceManager = deviceManager;
        this.serverComponent = serverComponent;
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
        count ++;
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        final String target = address.getHostString()+":"+address.getPort();
        if(idleTimesToClose == count){
            ctx.channel().close().addListener((ChannelFutureListener) future -> {
                if(future.isSuccess()){
                    //链接关闭的时候移除对应的设备
                    if(StringUtils.isNotBlank(equipCode)) {
                        deviceManager.remove(equipCode);
                        logger.warn("{} - 设备编号: {} - 客户端地址: {} 已超过{}次没有发送数据, 将主动下线设备(下线成功)", desc, this.equipCode, target, idleTimesToClose);
                    }

                } else {
                    logger.warn("{} - 设备编号: {} - 客户端地址: {} 已超过{}次没有发送数据, 将主动下线设备(下线失败)", desc, this.equipCode, target, idleTimesToClose);
                }
            });

            return;
        }

        if(logger.isWarnEnabled())
            logger.warn("{}告警 - 设备编号: {} - 客户端地址: {} 第{}次", desc, this.equipCode, target, count);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnParseBodyMessage msg) throws Exception {
        count = 0;
        if(msg.getHead() == null || msg.getHead().getEquipCode() == null) {
            logger.error("事件管理器错误, 没办法注册设备到设备管理器, 请检查报文是否包含报文头及报文头包含设备编号 - 报文: {}", msg);
            return;
        }

        //获取设备编号,验证此设备是否已经连接
        this.equipCode = msg.getHead().getEquipCode();

        ChannelPipeline pipeline = (ChannelPipeline) deviceManager.get(equipCode);

        //设备还没有注册到设备管理器,则注册
        if(null == pipeline){
            deviceManager.add(equipCode, ctx.pipeline());

            //触发设备上线事件
            IotServeBootstrap.publishApplicationEvent(new DeviceEvent(this.equipCode, DeviceEventType.设备上线));

            if(logger.isDebugEnabled()) {
                logger.debug("设备上线({}) - 设备编号: {} - 服务描述: {}", serverComponent.name()
                        , this.equipCode, serverComponent.deviceServer().desc());
            }
        } else { //设备已经存在,判断是否是同一台设备

            //不是同一台设备则关闭早期一台
            if(pipeline != ctx.pipeline()){
                pipeline.close().addListener((ChannelFutureListener) future -> {
                    String status = future.isSuccess() ? "关闭成功" : "关闭失败";
                    logger.warn("设备编号冲突 - 设备编号: {} - 处理方案: 移除早期的一台 - 状态：{}", equipCode, status);
                });

                // 先移除早期的一台
                deviceManager.remove(equipCode);
                // 新增最新的设备
                deviceManager.add(equipCode, ctx.pipeline());

                //触发设备上线事件
                IotServeBootstrap.publishApplicationEvent(new DeviceEvent(this.equipCode, DeviceEventType.设备上线));
            }
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(this.equipCode != null) {
            // 断线后移除设备
            deviceManager.remove(equipCode);

            //触发设备下线事件
            IotServeBootstrap.publishApplicationEvent(new DeviceEvent(this.equipCode, DeviceEventType.设备断线));

            logger.warn("设备断线({}) - 设备编号: {} - 客户端地址: {} - 说明: 移除从设备管理里面", serverComponent.name(), this.equipCode);
        } else {
            ctx.fireChannelInactive();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        IotServeBootstrap.publishApplicationEvent(new ExceptionEvent(cause, this.equipCode));
    }
}
