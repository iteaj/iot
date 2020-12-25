package com.iteaj.iot.client.mqtt.api;

import com.iteaj.iot.client.mqtt.common.MessageData;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface MqttConsumerProcess extends MqttConsumer {
	/**
	 * processSubAck
	 * @param messageId
	 */
	void processSubAck(int messageId);
	
	/**
	 * processUnSubBack
	 * @param messageId
	 */
	void processUnSubBack(int messageId);
	
	/**
	 * processPubRel
	 * @param messageId
	 */
	void processPubRel(int messageId);
	
	/**
	 * processPublish
	 * @param recviceMessage
	 */
	void processPublish(MessageData recviceMessage);
	
	/**
	 * sendPubRecMessage
	 * @param messageId
	 */
	void sendPubRecMessage(int messageId);
	
	/**
	 * sendPubAckMessage
	 * @param messageId
	 */
	void sendPubAckMessage(int messageId);
	
	/**
	 * sendPubCompMessage
	 * @param messageId
	 */
	void sendPubCompMessage(int messageId);
	
	/**
	 * saveMesage
	 * @param recviceMessage
	 */
    void saveMesage(MessageData recviceMessage);
    
    /**
     * delMesage
     * @param messageId
     */
    void delMesage(int messageId);
}
