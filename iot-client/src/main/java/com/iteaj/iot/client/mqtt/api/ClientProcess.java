package com.iteaj.iot.client.mqtt.api;

import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.mqtt.common.NettyUtil;
import com.iteaj.iot.client.mqtt.common.api.GlobalUniqueId;
import com.iteaj.iot.client.mqtt.common.api.GlobalUniqueIdImpl;
import com.iteaj.iot.client.mqtt.common.api.GlobalUniqueIdSet;
import com.iteaj.iot.client.mqtt.common.core.CacheList;
import com.iteaj.iot.client.mqtt.common.core.UniqueIdInteger;
import io.netty.channel.Channel;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class ClientProcess implements GlobalUniqueIdSet {
	private IotNettyClient client;
	private GlobalUniqueId globalUniqueId;

	public ClientProcess(IotNettyClient client) {
		globalUniqueId = new GlobalUniqueIdImpl();
		this.client = client;
	}
	
	@Override
	public void setGlobalUniqueId(GlobalUniqueId globalUniqueId) {
		if (globalUniqueId != null) {
			this.globalUniqueId = globalUniqueId;
		}
	}
	@Override
	public void setGlobalUniqueIdCache(CacheList<UniqueIdInteger> cacheList) {
		globalUniqueId.setCacheList(cacheList);
	}
	
	protected String getClientId() {
		return NettyUtil.getClientId(client.getChannel());
	}

	public Channel channel() {
		return client.getChannel();
	}

	public GlobalUniqueId messageId() {
		return globalUniqueId;
	}

}
