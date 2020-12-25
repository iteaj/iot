package com.iteaj.iot.client.mqtt.common;

import io.netty.handler.codec.mqtt.MqttVersion;

import java.util.Arrays;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MqttConnectOptions {
	private MqttVersion mqttVersion = MqttVersion.MQTT_3_1_1;

	private String clientIdentifier = "";
	private String willTopic="";
	private String userName="";
	private byte[] willMessage;
	private byte[] password;
	private int willQos=0;
	private int keepAliveTime=60;

	private boolean hasUserName = false;
	private boolean hasPassword = false;
	private boolean hasWillRetain = false;
	private boolean hasWillFlag = false;
	private boolean hasCleanSession = false;

	public MqttConnectOptions() {

	}

	public MqttVersion getMqttVersion() {
		return mqttVersion;
	}

	public void setMqttVersion(MqttVersion mqttVersion) {
		this.mqttVersion = mqttVersion;
	}

	public String getClientIdentifier() {
		return clientIdentifier;
	}

	public void setClientIdentifier(String clientIdentifier) {
		this.clientIdentifier = clientIdentifier;
	}

	public String getWillTopic() {
		return willTopic;
	}

	public void setWillTopic(String willTopic) {
		this.willTopic = willTopic;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public byte[] getWillMessage() {
		return willMessage;
	}

	public void setWillMessage(byte[] willMessage) {
		this.willMessage = willMessage;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public int getWillQos() {
		return willQos;
	}

	public void setWillQos(int willQos) {
		this.willQos = willQos;
	}

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public boolean isHasUserName() {
		return hasUserName;
	}

	public void setHasUserName(boolean hasUserName) {
		this.hasUserName = hasUserName;
	}

	public boolean isHasPassword() {
		return hasPassword;
	}

	public void setHasPassword(boolean hasPassword) {
		this.hasPassword = hasPassword;
	}

	public boolean isHasWillRetain() {
		return hasWillRetain;
	}

	public void setHasWillRetain(boolean hasWillRetain) {
		this.hasWillRetain = hasWillRetain;
	}

	public boolean isHasWillFlag() {
		return hasWillFlag;
	}

	public void setHasWillFlag(boolean hasWillFlag) {
		this.hasWillFlag = hasWillFlag;
	}

	public boolean isHasCleanSession() {
		return hasCleanSession;
	}

	public void setHasCleanSession(boolean hasCleanSession) {
		this.hasCleanSession = hasCleanSession;
	}

	@Override
	public String toString() {
		return "MqttConnectOptions{" +
				"mqttVersion=" + mqttVersion +
				", clientIdentifier='" + clientIdentifier + '\'' +
				", willTopic='" + willTopic + '\'' +
				", userName='" + userName + '\'' +
				", willMessage=" + Arrays.toString(willMessage) +
				", password=" + Arrays.toString(password) +
				", willQos=" + willQos +
				", keepAliveTime=" + keepAliveTime +
				", hasUserName=" + hasUserName +
				", hasPassword=" + hasPassword +
				", hasWillRetain=" + hasWillRetain +
				", hasWillFlag=" + hasWillFlag +
				", hasCleanSession=" + hasCleanSession +
				'}';
	}
}
