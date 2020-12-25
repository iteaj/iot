package com.iteaj.iot.client.mqtt.common.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class CacheListLocalMemory<V> extends BaseCacheList< V> {
	private  ConcurrentHashMap<String, V> msgList = new ConcurrentHashMap<String, V>();

	@Override
	public boolean put(String key, V value) {
		return msgList.put(key, value) == null;
	}
	
	@Override
	public V get(String key) {
		if (key == null) {
			return null;
		} else {
			return msgList.getOrDefault(key, null);
		}
	}
	
	@Override
	public V remove(String key) {
		return msgList.remove(key);
	}
	
	@Override
	public long size() {
		return msgList.size();
	}
	
	@Override
	public boolean exists(String key) {
		return msgList.containsKey(key);
	}
}
