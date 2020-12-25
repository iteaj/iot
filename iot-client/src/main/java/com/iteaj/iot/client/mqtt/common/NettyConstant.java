package com.iteaj.iot.client.mqtt.common;

import io.netty.util.AttributeKey;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class NettyConstant {
	public final static String SYS_LINE_FEED = System.getProperty("line.separator", "\n");
	public static final AttributeKey<String> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.cha");
	public static final AttributeKey<String> WEBSOCKET_KEY = AttributeKey.valueOf("cha.websocket");
	public static final AttributeKey<String> CLIENTID_KEY = AttributeKey.valueOf("cha.clientId");
	public static final String HANDLER_NAME_HEARTCHECK = "idle";
	public static final String HANDLER_NAME_SSL = "ssl";
}
