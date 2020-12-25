package com.iteaj.iot.client.mqtt.common.api;

import com.iteaj.iot.client.mqtt.common.core.CacheList;
import com.iteaj.iot.client.mqtt.common.core.UniqueIdInteger;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public interface GlobalUniqueIdSet {
	/**
	 * Custom set GlobalUniqueId object
	 * @param globalUniquedId
	 */
	public void setGlobalUniqueId(GlobalUniqueId globalUniquedId);
	
	/**
	 * 
	 * @param cacheList
	 */
	public void setGlobalUniqueIdCache(CacheList<UniqueIdInteger> cacheList);
}
