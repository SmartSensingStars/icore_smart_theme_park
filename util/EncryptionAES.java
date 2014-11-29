package com.larcloud.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Java版3DES加密解密，适用于PHP版3DES加密解密(PHP语言开发的MCRYPT_3DES算法、MCRYPT_MODE_ECB模式、
 * PKCS7填充方式)
 * 
 * @author G007N
 */
public class EncryptionAES {
	// private static SecretKey secretKey = null;// key对象
	// private static Cipher cipher = null; // 私鈅加密对象Cipher
	private static String keyString = createSecretKey();// 密钥
	private static Logger log = Logger.getRootLogger();

	// static {
	// try {
	// /* 密钥为16的倍数 */
	// /* AES算法 */
	// secretKey = new SecretKeySpec(keyString.getBytes(), "AES");// 获得密钥
	// /* 获得一个私鈅加密类Cipher，DESede-》AES算法，ECB是加密模式，PKCS5Padding是填充方式 */
	// cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	//
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// }
	// }

	/**
	 * 加密 外部包装
	 * 
	 * @param val
	 * @param key
	 * @return
	 */

	public static String encrypt(String val, String key) {
		return desEncrypt(val, key);
	}

	/**
	 * 解密 外部包装
	 * 
	 * @param msg
	 * @param key
	 * @return
	 */
	public static byte[] decrypt(String msg, String key) {
		byte[] s = null;
		try {
			s = desDecrypt(msg, key).getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 内部加密方法
	 * 
	 * @param message
	 * @param key
	 * @return
	 */
	public static String desEncrypt(String message, String key) {
		SecretKey secretKey = null;// key对象
		Cipher cipher = null; // 私鈅加密对象Cipher
		/* AES算法 */
		secretKey = new SecretKeySpec(key.getBytes(), "AES");// 获得密钥
		/* 获得一个私鈅加密类Cipher，DESede-》AES算法，ECB是加密模式，PKCS5Padding是填充方式 */
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String result = ""; // DES加密字符串
		String newResult = "";// 去掉换行符后的加密字符串
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey); // 设置工作模式为加密模式，给出密钥
			byte[] resultBytes = cipher.doFinal(message.getBytes("UTF-8")); // 正式执行加密操作
			BASE64Encoder enc = new BASE64Encoder();
			result = enc.encode(resultBytes);// 进行BASE64编码
			newResult = filter(result); // 去掉加密串中的换行符
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return newResult;
	}

	/**
	 * 解密
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String desDecrypt(String message, String key)
			throws Exception {
		SecretKey secretKey = null;// key对象
		Cipher cipher = null; // 私鈅加密对象Cipher
		/* AES算法 */
		secretKey = new SecretKeySpec(key.getBytes(), "AES");// 获得密钥
		/* 获得一个私鈅加密类Cipher，DESede-》AES算法，ECB是加密模式，PKCS5Padding是填充方式 */
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String result = "";
		try {
			BASE64Decoder dec = new BASE64Decoder();
			byte[] messageBytes = dec.decodeBuffer(message); // 进行BASE64编码
			cipher.init(Cipher.DECRYPT_MODE, secretKey); // 设置工作模式为解密模式，给出密钥
			byte[] resultBytes = cipher.doFinal(messageBytes);// 正式执行解密操作
			result = new String(resultBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 去掉加密字符串换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String filter(String str) {
		String output = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (asc != 10 && asc != 13) {
				sb.append(str.subSequence(i, i + 1));
			}
		}
		output = new String(sb);
		return output;
	}

	/**
	 * 加密解密测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String key = createSecretKey();
			String key = "AQ60AhQeLvkZaWND";
			String strText = "hello jnad";
			String deseResult = desEncrypt(strText, key);// 加密
			System.out.println("加密结果：" + deseResult);
			String desdResult = (desDecrypt(deseResult, key));// 解密
			System.out.println("解密结果：" + desdResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String createSecretKey() {
		String key = "";

		String[] keys = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6",
				"7", "8", "9", "0" };

		for (int i = 0; i < 16; i++) {
			key += keys[(int) (Math.random() * 62)];

		}

		return key;
	}
}
