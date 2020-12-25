package com.iteaj.iot.client.mqtt;

import com.iteaj.iot.client.mqtt.common.exception.LoginException;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public interface ClientProcess {
	/**
	 * 登录完成
	 * @param bResult
	 * @param exception
	 */
	void loginFinish(boolean bResult, LoginException exception);

	/**
	 * 发送指令关闭
	 */
	void disConnect();
}
