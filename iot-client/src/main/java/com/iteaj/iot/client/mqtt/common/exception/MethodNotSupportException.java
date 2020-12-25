package com.iteaj.iot.client.mqtt.common.exception;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class MethodNotSupportException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public MethodNotSupportException() {
		super(getMsgStr());
	}
	
	private static String getMsgStr() {
		String className = "";
		String methodName = "";
		Thread cur = Thread.currentThread();
		int iLevel = 3;
		if (cur != null) {
			StackTraceElement[] ary = cur.getStackTrace();
			if ((ary != null) && (ary.length > iLevel)) { 
				className = ary[iLevel].getClassName();
				methodName = ary[iLevel].getMethodName();
			}
		}
		
		return String.format("Method Not Support: %s-%s", className, methodName);
	}
}
