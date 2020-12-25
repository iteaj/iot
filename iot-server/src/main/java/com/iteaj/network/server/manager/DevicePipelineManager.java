package com.iteaj.network.server.manager;

import com.iteaj.network.ConcurrentStorageManager;
import com.iteaj.network.DeviceManager;
import com.iteaj.network.Protocol;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>通过设备编号获取以设备关联的链接信息{@link ChannelPipeline}</p>
 *  映射关系: key：为设备编号   value：{@link ChannelPipeline}
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public class DevicePipelineManager extends ConcurrentStorageManager<String, ChannelPipeline>
        implements DeviceManager<String, ChannelPipeline>, InitializingBean {

    private int initialCapacity; //初始化大小
    private static DevicePipelineManager mapperManager;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private DevicePipelineManager() {
        super(null);
    }

    /**
     * 获取一个设备管理实例
     * @return
     */
    public static DevicePipelineManager getInstance(){
        if(null != mapperManager)
            return mapperManager;

        synchronized (DevicePipelineManager.class){
            mapperManager = new DevicePipelineManager();
        }

        return mapperManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setMapper(new ConcurrentHashMap<String, ChannelPipeline>(initialCapacity));
        mapperManager = this;
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    @Override
    public ChannelFuture writeAndFlush(String equipCode, Object msg, Object... objects) {
        if(StringUtils.isBlank(equipCode))
            throw new IllegalArgumentException("设备编号不能为空");
        if(null == msg) {
            throw new IllegalArgumentException("请传入要发送的协议报文");
        }

        ChannelPipeline entries = get(equipCode);
        Protocol protocol = (Protocol) msg;
        if(null == entries) {
            logger.warn("无此设备或设备断线 设备编号: {} - 协议: {} - 协议说明: {}", equipCode, protocol.protocolType(), protocol.desc());
            return null;
        } else if(!entries.channel().isRegistered()) { // 设备已经取消注册, 删除设备
            remove(equipCode);
            logger.debug("-> 此设备已经断线(失活): {} - {}", equipCode, msg);
            return null;
        }

        return entries.writeAndFlush(msg);
    }
}
