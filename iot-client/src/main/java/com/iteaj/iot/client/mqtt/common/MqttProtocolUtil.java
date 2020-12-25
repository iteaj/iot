package com.iteaj.iot.client.mqtt.common;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;

import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttProtocolUtil {
	public static MqttUnsubAckMessage unsubAckMessage(int messageId) {
		return (MqttUnsubAckMessage) MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(messageId), null);
	}

	public static MqttSubAckMessage subAckMessage(int messageId, List<Integer> mqttQoSList) {
		return (MqttSubAckMessage) MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(messageId), new MqttSubAckPayload(mqttQoSList));
	}

	public static MqttMessage pubCompMessage(int messageId) {
		return MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(messageId), null);
	}

	public static MqttPubAckMessage pubAckMessage(int messageId) {
		return (MqttPubAckMessage) MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(messageId), null);
	}

	public static MqttMessage pubRecMessage(int messageId) {
		return MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(messageId), null);
	}

	public static MqttMessage pubRelMessage(int messageId, boolean isDup) {
		return MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.PUBREL, isDup, MqttQoS.AT_LEAST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(messageId), null);
	}

	public static MqttMessage pingRespMessage() {
		return MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0), null, null);
	}

	public static MqttConnAckMessage connAckMessage(MqttConnectReturnCode code, boolean sessionPresent) {
		return (MqttConnAckMessage) MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
				new MqttConnAckVariableHeader(code, sessionPresent), null);
	}

	public static MqttConnectReturnCode connectReturnCodeForException(Throwable cause) {
		MqttConnectReturnCode code = MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE;
		if (cause instanceof MqttUnacceptableProtocolVersionException) {
			// 不支持的协议版本
			code = MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION;
		} else if (cause instanceof MqttIdentifierRejectedException) {
			// 不合格的clientId
			code = MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED;
		} else {
			code = MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE;
		}
		return code;
	}

	public static MqttPublishMessage publishMessage(String topic, byte[] payload, int qosValue, int messageId,
			boolean isRetain) {
		return publishMessage(topic, false, qosValue, isRetain, messageId, payload);
	}

	public static MqttPublishMessage publishMessage(String topic, byte[] payload, int qosValue, boolean isRetain,
			int messageId, boolean isDup) {
		return publishMessage(topic, isDup, qosValue, isRetain, messageId, payload);
	}
	
	public static MqttPublishMessage publishMessage(String topicName, boolean isDup, int qosValue, boolean isRetain,
			int messageId, byte[] payload) {
		return (MqttPublishMessage) MqttMessageFactory.newMessage(
				new MqttFixedHeader(MqttMessageType.PUBLISH, isDup, MqttQoS.valueOf(qosValue), isRetain, 0),
				new MqttPublishVariableHeader(topicName, messageId), Unpooled.buffer().writeBytes(payload));
	}
	
	public static MqttSubscribeMessage subscribeMessage(List<MqttTopicSubscription> mqttTopicSubscriptions,
			int messageId) {
		MqttSubscribePayload mqttSubscribePayload = new MqttSubscribePayload(mqttTopicSubscriptions);
		MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE,
				false, 0);
		MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(messageId);
		return new MqttSubscribeMessage(mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubscribePayload);
	}

	public static MqttUnsubscribeMessage unSubscribeMessage(List<String> topic, int messageId) {
		MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBSCRIBE, false, MqttQoS.AT_MOST_ONCE,
				false, 0x02);
		MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(messageId);
		MqttUnsubscribePayload mqttUnsubscribeMessage = new MqttUnsubscribePayload(topic);
		return new MqttUnsubscribeMessage(mqttFixedHeader, variableHeader, mqttUnsubscribeMessage);
	}

	public static MqttMessage disConnectMessage() {
		MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.DISCONNECT, false, MqttQoS.AT_MOST_ONCE,
				false, 0x02);
		return new MqttMessage(mqttFixedHeader);
	}
}
