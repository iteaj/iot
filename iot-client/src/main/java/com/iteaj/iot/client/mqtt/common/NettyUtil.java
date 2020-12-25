package com.iteaj.iot.client.mqtt.common;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class NettyUtil {
	public static String getClientId(Channel channel) {
		Attribute<String> attr = channel.attr(NettyConstant.CLIENTID_KEY);
		return attr.get();
	}
	
	public static void setClientId(Channel channel, String clientId) {
		Attribute<String> attr = channel.attr(NettyConstant.CLIENTID_KEY);
		attr.set(clientId);
	}
	
	public static boolean isLogin(Channel channel) {
		String clientId = (String) channel.attr(NettyConstant.CLIENTID_KEY).get();
		return clientId != null && clientId.trim().length() > 0;
	}
}
