package com.iteaj.iot.client.mqtt.api;

import com.iteaj.iot.client.mqtt.common.MessageData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
public interface MqttProducerProcess extends MqttProducer {

	/**
	 * processPubAck
	 * @param messageId
	 */
	void processPubAck(int messageId);

	/**
	 * processPubRec
	 * @param messageId
	 */
	void processPubRec(int messageId);

	/**
	 * processPubComp
	 * @param messageId
	 */
	public void processPubComp(int messageId);

	/**
	 * sendPubRel
	 * @param messageId
	 */
	void sendPubRel(int messageId);

	/**
	 * saveMessage
	 * @param recviceMessage
	 */
	void saveMessage(MessageData recviceMessage);

	/**
	 * delMessage
	 * @param messageId
	 */
	void delMessage(int messageId);
}
