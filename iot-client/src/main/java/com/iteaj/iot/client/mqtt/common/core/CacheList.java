package com.iteaj.iot.client.mqtt.common.core;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface CacheList<V> {
	/**
	 * getCacheName
	 * @return
	 */
	public String getCacheName();
	
	/**
	 * put
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(String key, V value);
	
	/**
	 * get
	 * @param key
	 * @return
	 */
	public V get(String key);
	
	/**
	 * remove
	 * @param key
	 * @return
	 */
	public V remove(String key);
	
	/**
	 * exists
	 * @param key
	 * @return
	 */
	public boolean exists(String key);
	
	/**
	 * containsKey
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key);
	
	/**
	 * size
	 * @return
	 */
	public long size();
	

	///
	//public Set<Map.Entry<String, V>> entrySet();
	//public Collection<Object> valueList();
	//public Collection<Object> keyList();
}
