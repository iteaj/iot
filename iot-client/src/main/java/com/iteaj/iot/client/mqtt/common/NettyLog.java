package com.iteaj.iot.client.mqtt.common;

import java.text.SimpleDateFormat;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class NettyLog {
	private static class LogInfo {
		public String name;
		public String methodName;
		public long lineNumber;
		public String threadName;

		@Override
		public String toString() {
			//return " [" + threadName + "] " + name + " - " + methodName + "- " + lineNumber + "";
			return name;
		}
	}

	private static String getCurDateTimeStr() {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		return dateformat.format(System.currentTimeMillis());
	}

	private static LogInfo getEnterClassName() {
		Thread cur = Thread.currentThread();
		LogInfo logInfo = new LogInfo();
		int iLevel = 4;
		if (cur != null) {
			StackTraceElement[] ary = cur.getStackTrace();
			logInfo.threadName = cur.getName();
			if ((ary != null) && (ary.length > iLevel)) { 
				logInfo.name = ary[iLevel].getClassName();
				logInfo.methodName = ary[iLevel].getMethodName();
				logInfo.lineNumber = ary[iLevel].getLineNumber();
			}
		}
		return logInfo;
	}

	private static void baseinfo(String msg) {
		System.err.println(String.format("%s info - %s : %s", getCurDateTimeStr(), getEnterClassName(), msg));
	}

	private static void basedebug(String msg) {
		System.err.println(String.format("%s [DEBUG] - %s : %s", getCurDateTimeStr(), getEnterClassName(), msg));
	}

	private static void baseerror(String msg) {
		System.err.println(String.format("%s [ERROR] - %s : %s", getCurDateTimeStr(), getEnterClassName(), msg));
	}

	public static void info(String msg) {
		baseinfo(msg);
	}
	
	public static void info(String msg, Object... arguments) {
		String newMsg = msg.replaceAll("\\{\\}", "\\%s");
		baseinfo(String.format(newMsg, arguments));
	}
	
	public static void error(String msg) {
		baseerror(msg);

	}
	
	public static void error(String msg, Object... arguments) {
		String newMsg = msg.replaceAll("\\{\\}", "\\%s");
		baseerror(String.format(newMsg, arguments));
	}

	public static void error(String msg, Exception exp) {
		baseerror(msg);
		if (null != exp) {
			exp.printStackTrace();
		}
	}

	public static void debug(String msg) {
		basedebug(msg);
	}

	public static void debug(String msg, Object... arguments) {
		String newMsg = msg.replaceAll("\\{\\}", "\\%s");
		basedebug(String.format(newMsg, arguments));
	}
}
