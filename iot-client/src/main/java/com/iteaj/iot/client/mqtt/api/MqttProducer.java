package com.iteaj.iot.client.mqtt.api;

import com.iteaj.iot.client.mqtt.common.MessageData;
import com.iteaj.iot.client.mqtt.common.api.GlobalUniqueIdSet;
import com.iteaj.iot.client.mqtt.common.core.CacheList;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public interface MqttProducer extends GlobalUniqueIdSet {
	/**
	 * 自定义缓存
	 * @param msgList
	 */
	void setCacheList(CacheList<MessageData> msgList);


	/**
	 * 发布消息
	 * @param topic
	 * @param message
	 * @param qosValue
	 * @param isRetain
	 */
	void sendPublishMessage(String topic, String message, int qosValue, boolean isRetain);


}
