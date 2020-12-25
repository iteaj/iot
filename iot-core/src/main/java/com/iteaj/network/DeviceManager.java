package com.iteaj.network;

import io.netty.channel.ChannelFuture;

/**
 * <p>设备链接管理</p>
 * 用来管理设备和链接的映射关系
 * Create Date By 2017-09-12
 * @author iteaj
 * @since 1.7
 */
public interface DeviceManager<T, K> extends StorageManager<T,K> {

    /**
     * @param equipCode
     * @param msg
     * @return
     */
    ChannelFuture writeAndFlush(String equipCode, Object msg, Object... objects);
}
