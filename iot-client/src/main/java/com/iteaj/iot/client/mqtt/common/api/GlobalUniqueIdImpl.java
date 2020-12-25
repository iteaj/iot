package com.iteaj.iot.client.mqtt.common.api;


import com.iteaj.iot.client.mqtt.common.core.CacheList;
import com.iteaj.iot.client.mqtt.common.core.CacheListLocalMemory;
import com.iteaj.iot.client.mqtt.common.core.UniqueIdInteger;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class GlobalUniqueIdImpl implements GlobalUniqueId {	
	private CacheList<UniqueIdInteger> list = new CacheListLocalMemory<UniqueIdInteger>();
	
	public void setCacheList(CacheList<UniqueIdInteger> cacheList) {
		this.list =  cacheList;
	}
	
	@Override
	public int getNextMessageId(String clientId) {	
		UniqueIdInteger value = list.get(clientId);
		if (value == null) {
			value = new UniqueIdInteger();
		} else {
			value.addInc();
		}
		list.put(clientId, value);
		return value.id();
	}
}
