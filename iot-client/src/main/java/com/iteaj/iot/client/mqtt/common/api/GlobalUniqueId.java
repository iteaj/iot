package com.iteaj.iot.client.mqtt.common.api;

import com.iteaj.iot.client.mqtt.common.core.CacheList;
import com.iteaj.iot.client.mqtt.common.core.UniqueIdInteger;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface GlobalUniqueId {
	
	void setCacheList(CacheList<UniqueIdInteger> cacheList);
	/**
	 * get uniuqe id
	 * @return
	 */
	int getNextMessageId(String clientId);
}
