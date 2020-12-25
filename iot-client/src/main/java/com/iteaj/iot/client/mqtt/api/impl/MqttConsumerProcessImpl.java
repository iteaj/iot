package com.iteaj.iot.client.mqtt.api.impl;

import com.iteaj.iot.client.IotNettyClient;
import com.iteaj.iot.client.mqtt.api.ClientProcess;
import com.iteaj.iot.client.mqtt.api.MqttConsumer;
import com.iteaj.iot.client.mqtt.api.MqttConsumerListener;
import com.iteaj.iot.client.mqtt.api.MqttConsumerProcess;
import com.iteaj.iot.client.mqtt.common.MessageData;
import com.iteaj.iot.client.mqtt.common.MessageStatus;
import com.iteaj.iot.client.mqtt.common.NettyLog;
import com.iteaj.iot.client.mqtt.common.SubscribeMessage;
import com.iteaj.iot.client.mqtt.protocol.ClientProtocolUtil;
import com.iteaj.iot.client.mqtt.common.core.CacheList;
import com.iteaj.iot.client.mqtt.common.core.CacheListLocalMemory;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttConsumerProcessImpl extends ClientProcess implements MqttConsumerProcess, MqttConsumer {

	private List<MqttConsumerListener> listeners;

	private CacheList<MessageData> msgListCache;
	
	public MqttConsumerProcessImpl(IotNettyClient client) {
		this(client, new CacheListLocalMemory<>());
	}
	
	public MqttConsumerProcessImpl(IotNettyClient client, CacheList<MessageData> msgListCache) {
		super(client);
		this.msgListCache = msgListCache;
	}

	@Override
	public void subscribe(String topic, int qosValue) {
		SubscribeMessage msgObj = SubscribeMessage.builder().topic(topic).qos(qosValue);
		subscribe(msgObj);
	}

	@Override
	public void setConsumerListener(List<MqttConsumerListener> listeners) {
		this.listeners = listeners;
	}

	public void subscribe(SubscribeMessage info) {
		NettyLog.debug("subscribe： {} ", info);
		subscribe(new SubscribeMessage[] { info });
	}

	private void subscribe(SubscribeMessage... info) {
		if (info != null) {
			int messageId = messageId().getNextMessageId(this.getClientId());
			if (!channel().isActive()) {
				NettyLog.debug("channel is close");
				return;
			}
			
			channel().writeAndFlush(ClientProtocolUtil.subscribeMessage(messageId, info));
		}
	}

	public void unSubscribe(String topic) {
		List<String> topics = new ArrayList<String>();
		topics.add(topic);
		unSubscribe(topics);
	}

	public void unSubscribe(List<String> topics) {
		if (topics != null) {
			int messageId = messageId().getNextMessageId(this.getClientId());
			channel().writeAndFlush(ClientProtocolUtil.unSubscribeMessage(topics, messageId));
		}
	}

	@Override
	public void saveMesage(MessageData recviceMessage) {
		//if (msgListCache == null) {return;}
		if ((recviceMessage != null) && (recviceMessage.getMessageId() > 0)) {
			NettyLog.debug("saveMessage: {}", recviceMessage.getMessageId());
			msgListCache.put(String.valueOf(recviceMessage.getMessageId()), recviceMessage);
		}
	}

	@Override
	public void delMesage(int messageId) {
		if (msgListCache == null) {return;}
		if (messageId > 0) {
			NettyLog.debug("delMesage: {}", messageId);
			msgListCache.remove(String.valueOf(messageId));
		}
	}

	public MessageData changeMessageStatus(int messageId, MessageStatus status) {
		if (msgListCache == null) {return null;}
		NettyLog.debug("changeMessageStatus: {}", status.name());

		MessageData msgObj = msgListCache.get(String.valueOf(messageId));
		if (msgObj != null) {
			msgObj.status(status);
			msgListCache.put(String.valueOf(messageId), msgObj);
		}
		return msgObj;
	}

	@Override
	public void processPubRel(int messageId) {
		MessageData msgObj = changeMessageStatus(messageId, MessageStatus.PUBREL);

		if ((msgObj != null) && (getListeners() != null)) {
			if (msgObj.getQos() == MqttQoS.EXACTLY_ONCE.value()) {
				getListeners().stream().filter(item -> item.matcher(msgObj.getTopic())).forEach(listener -> {
					listener.receiveMessage(msgObj.getMessageId(), msgObj.getTopic(), msgObj);
				});
			}
		}
		NettyLog.debug("process Pub-rel: messageId - {} ", messageId);
	}

	@Override
	public void processSubAck(int messageId) {
		NettyLog.debug("process Sub-ack: messageId - {} ", messageId);
	}

	@Override
	public void processUnSubBack(int messageId) {
		NettyLog.debug("process Un-sub-back: messageId - {} ", messageId);
	}

	@Override
	public void processPublish(MessageData msg) {
		NettyLog.info("process Publish: {} ", msg);

		if (getListeners() != null) {
			getListeners().stream().filter(item -> item.matcher(msg.getTopic())).forEach(listener -> {

				listener.receiveMessageByAny(msg.getMessageId(), msg.getTopic(), msg);

				if (msg.getQos() == MqttQoS.AT_MOST_ONCE.value() ||
						msg.getQos() == MqttQoS.EXACTLY_ONCE.value()) {
					listener.receiveMessage(msg.getMessageId(), msg.getTopic(), null);
				}
			});

		}
	}

	@Override
	public void sendPubRecMessage(int messageId) {
		NettyLog.debug("send Pub-rec: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubRecMessage(messageId));
	}

	@Override
	public void sendPubAckMessage(int messageId) {
		NettyLog.debug("send Pub-ack: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubAckMessage(messageId));
	}

	@Override
	public void sendPubCompMessage(int messageId) {
		NettyLog.debug("send Pub-comp: messageId - {} ", messageId);
		channel().writeAndFlush(ClientProtocolUtil.pubCompMessage(messageId));
	}

	public List<MqttConsumerListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<MqttConsumerListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void setCacheList(CacheList<MessageData> msgList) {
		if (msgList != null) {
			this.msgListCache = msgList;
		}
	}
}
