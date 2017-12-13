package com.bool.spmcache.utils;

import java.security.MessageDigest;


/**
 * MD5加密工具类，用于简单的缓存键MD5
 * ClassName: MD5Util <br/>
 * date: 2017年12月12日 下午5:46:12 <br/>
 *
 * @author Bool
 * @version
 */
public class MD5Util {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };
	
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resSb.append(byteToHexString(b[i]));
		}
		return resSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5(String str) {
		String resStr = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resStr = byteArrayToHexString(md.digest(str.getBytes("utf-8")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resStr;
	}


}
