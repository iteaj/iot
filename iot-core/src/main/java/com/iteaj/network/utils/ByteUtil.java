package com.iteaj.network.utils;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteUtil {
	private static final String TAG = "RZZ.ByteUtil";



	/*
	 * 把16进制字符串转换成字节数组
	 * @param hex
	 * @return
	 */
	public static byte[] hexToByte(String hexStr) {
		if(StringUtils.isBlank(hexStr)) {
			return null;
		}
		if(hexStr.length()%2 != 0) {//长度为单数
			hexStr = "0" + hexStr;//前面补0
		}

		char[] chars = hexStr.toCharArray();
		int len = chars.length/2;
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++) {
			int x = i*2;
			bytes[i] = (byte)Integer.parseInt(String.valueOf(new char[]{chars[x], chars[x+1]}), 16);
		}
		return bytes;

	}

	public static byte getByte(byte[] src, int offset) {
		return src[offset];
	}

	/**
	 * 字节数组转16进制
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHex(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 字节数组转16进制
	 * @param src
	 * @return
	 */
	public static final String bytesToHex(byte[] src, int offset, int length) {
		byte[] bArray = ArrayUtils.subarray(src, offset, offset + length);
		return bytesToHex(bArray);
	}

	public static final String intToHex(int value) {
		String s = Integer.toHexString(value);
		if(s.length() == 1) return "0"+s;
		else return s;
	}

	public static final String byteToHex(byte value) {
		String s = Integer.toHexString(0xff & value);
		if(s.length() == 1) return "0"+s;
		return s;
	}

	public static final String shortToHex(short value) {
		String s = Integer.toHexString(value);
		switch (s.length()) {
			case 1: return "000" + s;
			case 2: return "00" + s;
			case 3: return "0" + s;
			default: return s;
		}
	}

	/**
	 * @函数功能: BCD码转为10进制串(阿拉伯数据)
	 * @输入参数: BCD码
	 * @输出结果: 10进制串
	 */
	public static String bcdToStr(byte[] bytes){
		StringBuffer temp=new StringBuffer(bytes.length*2);

		for(int i=0;i<bytes.length;i++){
			temp.append((byte)((bytes[i] & 0xf0)>>>4));
			temp.append((byte)(bytes[i] & 0x0f));
		}

		return temp.toString();
	}

	/**
	 *
	 * @param src 原报文
	 * @param offset 起始位置
	 * @param length 长度
	 * @return
	 */
	public static String bcdToStr(byte[] src, int offset, int length){
		byte[] bArray = ArrayUtils.subarray(src, offset, offset + length);
		return bcdToStr(bArray);
	}

	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length()/2; p++) {
			if ( (abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ( (abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ( (abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ( (abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			}else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序
	 *
	 * @see #bytesToInt(byte[])
	 * @param value
	 *            要转换的int值
	 * @return byte数组
	 */
	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，地位在后)的顺序
	 *
	 * @see #bytesToIntOfNegate(byte[])
	 * @param value
	 *            要转换的int值
	 * @return byte数组
	 */
	public static byte[] intToBytesOfNegate(int value) {
		byte[] src = new byte[4];
		src[0] = (byte) ((value >> 24) & 0xFF);
		src[1] = (byte) ((value >> 16) & 0xFF);
		src[2] = (byte) ((value >> 8) & 0xFF);
		src[3] = (byte) (value & 0xFF);
		return src;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
	 *
	 * @param src
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return int数值
	 */
	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
		return value;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
	 *
	 * @param src
	 *            byte数组
	 * @return int数值
	 */
	public static int bytesToInt(byte[] src) {
		int value;
		value = ((src[0] & 0xFF) | ((src[1] & 0xFF) << 8)
				| ((src[2] & 0xFF) << 16) | ((src[3] & 0xFF) << 24));
		return value;
	}

	/**
	 * byte数组中取int数值，本方法适用于(高位在前，低位在后)的顺序
	 * @param src
	 * @return
	 */
	public static int bytesToIntOfNegate(byte[] src) {
		return ((src[3] & 0xFF) | ((src[2] & 0xFF) << 8)
				| ((src[1] & 0xFF) << 16) | ((src[0] & 0xFF) << 24));
	}

	/**
	 * byte数组中取int数值，本方法适用于(高位在前，低位在后)的顺序
	 * @param src
	 * @return
	 */
	public static int bytesToIntOfNegate(byte[] src, int offset) {
		src = subBytes(src, offset, offset + 4);
		return ((src[3] & 0xFF) | ((src[2] & 0xFF) << 8)
				| ((src[1] & 0xFF) << 16) | ((src[0] & 0xFF) << 24));
	}

	/**
	 * byte数组中取double数值，本方法适用于(低位在前，高位在后)的顺序
	 *
	 * @param src
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return double数值
	 */
	public static double bytesToDouble(byte[] src, int offset) {
		long value = 0;
		for (int i = 0; i < 8; i++) {
			value |= ((long) (src[offset+i] & 0xff)) << (8 * i);
		}

		return Double.longBitsToDouble(value);
	}

	/**
	 * 去掉字节数组尾数为零的字节,并将其转成字符串
	 * @param src
	 * @param charset
	 * @return
	 */
	public static String bytesToString(byte[] src, Charset charset){
		int search = Arrays.binarySearch(src, (byte) 0);
		return new String(Arrays.copyOf(src, search), charset);
	}

	/**
	 * 去掉字节数组尾数为零的字节,并将其转成字符串
	 * @param src
	 * @return
	 */
	public static String bytesToString(byte[] src){
		return new String(wipeLastZero(src));
	}

	/**
	 * 去掉字节数组尾数为零的字节,并将其转成字符串
	 * @param src
	 * @return
	 */
	public static String bytesToString(byte[] src, int startIndex, int endIndex){
		return new String(wipeLastZero(subBytes(src, startIndex, endIndex)));
	}

	/**
	 * 去除包含0的字节
	 * @param src
	 * @return
	 */
	private static byte[] wipeLastZero(byte[] src){
		int index = 0;
		for(int i=0; i<src.length; i++){
			if(src[i] == 0) {
				break;
			}

			index++;
		}

		return subBytes(src, 0, index);
	}

	public static byte[] longToBytes(long num) {
		byte[] byteNum = new byte[8];
		for (int ix = 0; ix < 8; ++ix) {
			byteNum[ix] = (byte)(num & 0xff);
			num >>= 8;
			//int offset = 64 - (ix + 1) * 8;
			//byteNum[ix] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}

	public static long bytesToLong(byte[] byteNum) {
		long num = 0;
		for (int ix = 0; ix < 8; ++ix) {
			num <<= 8;
			num |= (byteNum[ix] & 0xff);
		}
		return num;
	}

	public static long bytesToLong(byte[] byteNum,int offset) {
		long num = 0;
		for (int ix = 0; ix < 8; ++ix) {
			num <<= 8;
			num |= (byteNum[ix+offset] & 0xff);
		}
		return num;
	}

	/**
	 * 字符数组 切割
	 *
	 * @param bytes
	 *            字节数组
	 * @param beginIndex
	 *            开始索引
	 * @param endIndex
	 *            结束索引
	 * @return
	 */
	public static byte[] subBytes(byte[] bytes, int beginIndex, int endIndex) {
		if (bytes == null) {
			return new byte[0];
		}
		int size = bytes.length;
		if (beginIndex < 0 || beginIndex > endIndex || size < endIndex) {
			return new byte[0];
		}
		int newSize = endIndex - beginIndex;
		byte[] newBytes = new byte[newSize];
		int loop = 0;
		for (int i = beginIndex; i < endIndex; i++) {
			newBytes[loop] = bytes[i];
			loop++;
		}
		return newBytes;
	}

	public static byte[] subBytes(byte[] bytes, int beginIndex) {
		int size = bytes.length;
		int endIndex = size;
		return subBytes(bytes, beginIndex, endIndex);
	}

	/**
	 * 将截取数组指定数据,并将其转为字符串
	 * @param bytes
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	public static String subBytesToString(byte[] bytes, int beginIndex, int endIndex){
		byte[] subBytes = subBytes(bytes, beginIndex, endIndex);
		return bytesToString(subBytes);
	}

	/**
	 * byte数组添加整个target数组
	 *
	 *            源字节数组
	 * @param targetBytes
	 *            添加对象数组
	 * @param beginIndex
	 *            开始下标
	 * @return
	 */
	public static byte[] addBytes(byte[] sourceBytes, byte[] targetBytes,
			int beginIndex) {
		if (targetBytes == null) {
			return sourceBytes;
		}
		int targetSize = targetBytes.length;
		if (sourceBytes == null) {
			beginIndex = 0;
			sourceBytes = new byte[targetSize];
		}
		int sourceSize = sourceBytes.length;
		if (sourceSize - beginIndex < targetSize) {
			return sourceBytes;
		} else {
			for (int i = 0; i < targetSize; i++) {
				sourceBytes[beginIndex + i] = targetBytes[i];
			}
		}
		return sourceBytes;

	}


	public static String ByteToString(byte b){
		String s = Integer.toHexString(b & 0xFF);
		while (s.length()<2){
			s = "0"+ s;
		}
		return s.toUpperCase();
	}


	public static byte[] File2byte(String filePath)
	{
		byte[] buffer = null;
		try
		{
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1)
			{
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return buffer;
	}

	public static void byte2File(byte[] buf, String filePath, String fileName)
	{
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try
		{
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory())
			{
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (bos != null)
			{
				try
				{
					bos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 低位在前, 高位在后
	 * @param message
	 * @param offset
	 * @return
	 */
	public static short bytesToShort(byte[] message, int offset) {
		byte[] src = subBytes(message, offset, offset + 2);
		return (short) (((src[0] & 0xFF) << 8) | ((src[1] & 0xFF)));
	}

	/**
	 * 高位在前, 地位在后
	 * @param message
	 * @param offset
	 * @return
	 */
	public static short bytesToShortOfNegate(byte[] message, int offset) {
		byte[] src = subBytes(message, offset, offset + 2);
		return (short) (((src[1] & 0xFF) << 8) | ((src[0] & 0xFF)));
	}

	/**
	 *
	 * @param value
	 *            要转换的int值
	 * @return byte数组
	 */
	public static byte[] shortToBytes(short value) {
		byte[] src = new byte[2];
		src[0] = (byte) ((value >> 8) & 0xFF);
		src[1] = (byte) (value & 0xFF);
		return src;
	}

	/**
	 *
	 * @param value
	 *            要转换的int值
	 * @return byte数组
	 */
	public static byte[] shortToBytesOfNegate(short value) {
		byte[] src = new byte[2];
		src[0] = (byte) (value & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		return src;
	}

	/*public static void main(String[] args) {
		System.out.println(intToBytes(2000));
		if("2".equals("2\u0020".trim())){
			System.out.println(22222222);
		}
		System.out.println("ddd\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020".trim());
		byte[] bytes1 = intToBytes(2000);
		byte[] bytes2 = intToBytes(2000);
		System.out.println(bytesToInt(bytes1, 0));
		System.out.println(bytesToInt(bytes2, 0));
	}*/

}
