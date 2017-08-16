package com.yanbao.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

	public static String md5(String plainText) {
		String s = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			s = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String MD5Encode(String plainText, String salt) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(plainText.getBytes("UTF8"));
			byte[] temp;
			temp = md5.digest(salt.getBytes("UTF8"));
			return byte2hex(temp);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

	public static void main(String[] args) {
		System.out.println(Md5Util.MD5Encode("as12345","20170309"));
		System.out.println(Md5Util.MD5Encode("123456","20170309"));
	}
}
