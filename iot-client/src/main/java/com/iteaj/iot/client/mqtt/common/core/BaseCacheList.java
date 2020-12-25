package com.iteaj.iot.client.mqtt.common.core;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public abstract class BaseCacheList<V> implements CacheList<V> {
	private String cacheName;
	public BaseCacheList() {
	}
	public BaseCacheList(String cacheName) {
		this.cacheName = cacheName;
	}
	
	@Override
	public String getCacheName() {
		return cacheName;
	}
	
	@Override
	public boolean containsKey(String key) {
		return exists(key);
	}
}
