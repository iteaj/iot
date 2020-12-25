package com.iteaj.iot.client.mqtt.api;

import com.iteaj.iot.client.mqtt.common.MessageData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface MqttConsumerListener {

	/**
	 * 要监听的主题
	 * @return
	 */
	boolean matcher(String topic);

	/**
	 * 接收到的消息(确认过的)
	 * @param msgId
	 * @param topic
	 * @param msg
	 */
	void receiveMessage(int msgId, String topic, MessageData msg);
	

	/**
	 * 接收到的消息(收到就转发)
	 * @param msgId
	 * @param topic
	 * @param msg
	 */
	void receiveMessageByAny(int msgId, String topic, MessageData msg);
}
