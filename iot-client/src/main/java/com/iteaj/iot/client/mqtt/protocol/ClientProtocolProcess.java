package com.iteaj.iot.client.mqtt.protocol;

import com.iteaj.iot.client.mqtt.ClientProcess;
import com.iteaj.iot.client.mqtt.api.MqttConsumerProcess;
import com.iteaj.iot.client.mqtt.api.MqttProducerProcess;
import com.iteaj.iot.client.mqtt.common.MessageData;
import com.iteaj.iot.client.mqtt.common.MessageStatus;
import com.iteaj.iot.client.mqtt.common.NettyLog;
import com.iteaj.iot.client.mqtt.common.exception.LoginException;
import com.iteaj.network.utils.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;

/**
 * @author ben
 * @Title: basic
 * @Description:
 * B-borker; S-Subribler; P-Publisher
 **/

public class ClientProtocolProcess {
	private final ClientProcess clientProcess;
	private final MqttConsumerProcess consumerProcess;
	private final MqttProducerProcess producerProcess;

	public ClientProtocolProcess(ClientProcess clientProcess, MqttConsumerProcess consumerProcess,
								 MqttProducerProcess producerProcess) {
		this.clientProcess = clientProcess;
		this.consumerProcess = consumerProcess;
		this.producerProcess = producerProcess;
	}

	/**
	 * B - S, B - P
	 * @param channel
	 * @param msg
	 */
	public void processConnectBack(Channel channel, MqttConnAckMessage msg) {
		MqttConnAckVariableHeader mqttConnAckVariableHeader = msg.variableHeader();
		String sErrorMsg = "";
		switch (mqttConnAckVariableHeader.connectReturnCode()) {
		case CONNECTION_ACCEPTED:
			clientProcess.loginFinish(true, null);
			return;
		case CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD:
			sErrorMsg = "用户名密码错误";
			break;
		case CONNECTION_REFUSED_IDENTIFIER_REJECTED:
			sErrorMsg = "clientId不允许链接";
			break;
		case CONNECTION_REFUSED_SERVER_UNAVAILABLE:
			sErrorMsg = "服务不可用";
			break;
		case CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION:
			sErrorMsg = "mqtt 版本不可用";
			break;
		case CONNECTION_REFUSED_NOT_AUTHORIZED:
			sErrorMsg = "未授权登录";
			break;
		default:
			break;
		}

		clientProcess.loginFinish(false, new LoginException(sErrorMsg));
		channel.close();
	}

	/**
	 * B - P (Qos1)
	 * @param channel
	 * @param mqttMessage
	 */
	public void processPubAck(Channel channel, MqttMessage mqttMessage) {
		MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage
				.variableHeader();
		int messageId = messageIdVariableHeader.messageId();

		producerProcess.processPubAck(messageId);
		producerProcess.delMessage(messageId);
	}

	
	/**
	 * B- P(Qos2)
	 * @param channel
	 * @param mqttMessage
	 */
	public void processPubRec(Channel channel, MqttMessage mqttMessage) {
		MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage
				.variableHeader();
		int messageId = messageIdVariableHeader.messageId();

		producerProcess.processPubRec(messageId);
		producerProcess.sendPubRel(messageId);
	}

	/**
	 * B - P (Qos2)
	 * @param channel
	 * @param mqttMessage
	 */
	public void processPubComp(Channel channel, MqttMessage mqttMessage) {
		MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage
				.variableHeader();
		int messageId = messageIdVariableHeader.messageId();

		producerProcess.processPubComp(messageId);
		producerProcess.delMessage(messageId);
	}

	/**
	 * B - S(Qos2)
	 * @param channel
	 * @param mqttMessage
	 */
	public void processPubRel(Channel channel, MqttMessage mqttMessage) {
		MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage
				.variableHeader();
		int messageId = messageIdVariableHeader.messageId();
		this.consumerProcess.processPubRel(messageId);
		this.consumerProcess.sendPubCompMessage(messageId);
		this.consumerProcess.delMesage(messageId);
	}

	
	/**
	 * B - S(Qos0, Qos1, Qos2)
	 * @param channel
	 * @param mqttMessage
	 */
	public void processPublish(Channel channel, MqttPublishMessage mqttMessage) {
		MqttFixedHeader mqttFixedHeader = mqttMessage.fixedHeader();
		MqttPublishVariableHeader mqttPublishVariableHeader = mqttMessage.variableHeader();
		ByteBuf payload = mqttMessage.payload();
		String topciName = mqttPublishVariableHeader.topicName();
		MqttQoS qosLevel = mqttFixedHeader.qosLevel();
		int messageId = mqttPublishVariableHeader.packetId();
		byte[] bytes = ByteBufUtil.copyByteBuf(payload);

		MessageData recviceMessage = MessageData.builder().topic(topciName).qos(qosLevel.value())
				.messageId(messageId).payload(bytes)
				.status(MessageStatus.PUB)
				.dup(mqttFixedHeader.isDup())
				.retained(mqttFixedHeader.isRetain());

		if (qosLevel == MqttQoS.EXACTLY_ONCE) {
			this.consumerProcess.saveMesage(recviceMessage);
		}

		this.consumerProcess.processPublish(recviceMessage);

		switch (qosLevel) {
			case AT_MOST_ONCE:
				break;
			case AT_LEAST_ONCE:
				this.consumerProcess.sendPubAckMessage(messageId);
				break;
			case EXACTLY_ONCE:
				this.consumerProcess.sendPubRecMessage(messageId);
				break;
			default:
				break;
		}
	}
	
	/**
	 * B - P
	 * @param channel
	 * @param mqttMessage
	 */
	public void processSubAck(Channel channel, MqttSubAckMessage mqttMessage) {
		int messageId = mqttMessage.variableHeader().messageId();
		this.consumerProcess.processSubAck(messageId);
	}

	/**
	 * B - S
	 * @param channel
	 * @param mqttMessage
	 */
	public void processUnSubBack(Channel channel, MqttMessage mqttMessage) {
		int messageId;
		if (mqttMessage instanceof MqttUnsubAckMessage) {
			MqttUnsubAckMessage mqttUnsubAckMessage = (MqttUnsubAckMessage) mqttMessage;
			messageId = mqttUnsubAckMessage.variableHeader().messageId();
		} else {
			MqttMessageIdVariableHeader o = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();
			messageId = o.messageId();
			NettyLog.error("not UnsubAckMessage:{}", messageId);
		}
		this.consumerProcess.processUnSubBack(messageId);
	}
}
