package com.iteaj.iot.client.mqtt.common;

import com.iteaj.iot.client.mqtt.api.ClientProcess;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MessageData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String topic;
	private byte[] payload;

	private int qos;
	private boolean dup;
	private int messageId;
	private boolean retained;

	private long timestamp = System.currentTimeMillis();

	private volatile MessageStatus status;

	public static MessageData builder() {
		return new MessageData();
	}

	public String getTopic() {
		return topic;
	}

	public MessageData topic(String topic) {
		this.topic = topic;
		return this;
	}

	public byte[] getPayload() {
		return payload;
	}

	public MessageData payload(byte[] payload) {
		this.payload = payload;
		return this;
	}

	public int getQos() {
		return qos;
	}

	public MessageData qos(int qos) {
		this.qos = qos;
		return this;
	}

	public boolean isDup() {
		return dup;
	}

	public MessageData dup(boolean dup) {
		this.dup = dup;
		return this;
	}

	public int getMessageId() {
		return messageId;
	}

	public MessageData messageId(int messageId) {
		this.messageId = messageId;
		return this;
	}

	public boolean isRetained() {
		return retained;
	}

	public MessageData retained(boolean retained) {
		this.retained = retained;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public MessageData timestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public MessageData status(MessageStatus status) {
		this.status = status;
		return this;
	}

	@Override
	public String toString() {
		return "MessageData{" +
				"topic='" + topic + '\'' +
				", payload=" + Arrays.toString(payload) +
				", qos=" + qos +
				", dup=" + dup +
				", messageId=" + messageId +
				", retained=" + retained +
				", timestamp=" + timestamp +
				", status=" + status +
				'}';
	}
}
