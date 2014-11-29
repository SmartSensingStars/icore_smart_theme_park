package com.larcloud.util;

/**
 * 数组显示部分ֵ
 * 
 * @author jnad
 * 
 */
public class ShowArray {

	public static String showIP(byte[] b) {
		return byteToint(b[0]) + "." + byteToint(b[1]) + "." + byteToint(b[2])
				+ "." + byteToint(b[3]);
	}

	/**
	 * 将长度为2的Byte转为int
	 * 
	 * @param b
	 * @return
	 */
	public static int getByteToInt2(byte[] b, int point) {

		return (byteToint(b[point]) << 8) + byteToint(b[point + 1]);
	}

	private static int byteToint(byte b) {
		if (b >= 0) {
			return b;
		} else {
			return b + 0x100;
		}
	}
}
