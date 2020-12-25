package com.iteaj.iot.client.mqtt.api;

import com.iteaj.iot.client.mqtt.common.MessageData;
import com.iteaj.iot.client.mqtt.common.api.GlobalUniqueIdSet;
import com.iteaj.iot.client.mqtt.common.core.CacheList;

import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface MqttConsumer extends GlobalUniqueIdSet {
	/**
	 * 自定义缓存
	 * @param msgList
	 */
	public void setCacheList(CacheList<MessageData> msgList);
	
	/**
	 * 订阅主题
	 * @param topic
	 * @param qosValue
	 */
	public void subscribe(String topic, int qosValue);
	
	/**
	 * 接收订阅主题消息
	 * @param listeners
	 */
	public void setConsumerListener(List<MqttConsumerListener> listeners);
	

}
