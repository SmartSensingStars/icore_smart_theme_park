package com.larcloud.util;

/**
 * 字符集操作类
 * 
 * @author jnad
 * 
 */
public class CharUtil {

	/**
	 * 将两位可见字符转换为一位不可见字符
	 * 
	 * @param str
	 * @return
	 */

	public static byte[] hextobin(String str) {
		final byte[] _temp = str.getBytes();
		byte[] _out = new byte[_temp.length / 2];
		int _h = 0, _l = 0;
		for (int i = _temp.length - 2; i >= 0; i -= 2) {
			// 赋值=
			_h = _temp[i] - '0';
			_l = _temp[i + 1] - '0';
			// 校验是否有值在"A"-"F"中,有则转换
			if (_h > 9) {
				_h = _h % 16 + 9;
			}
			if (_l > 9) {
				_l = _l % 16 + 9;
			}
			_out[i / 2] = (byte) ((_h << 4) + _l);
		}
		return _out;
	}

	private static final void sleep() {
		try {
			Thread.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将2位可见字符,转成一位不可见字符(char)
	 * 
	 * @param str
	 * @return
	 */
	public static char[] strtohex(String str) {
		str = str.toUpperCase();
		char out[] = new char[str.length() / 2];
		char ch[] = str.toCharArray();
		char c = 0x00;
		for (int i = 0; i < str.length() / 2; i++) {
			ch[i * 2] = (char) (ch[i * 2] < 'A' ? ch[i * 2] - '0'
					: ch[i * 2] + 10 - 'A');
			ch[i * 2 + 1] = (char) (ch[i * 2 + 1] < 'A' ? ch[i * 2 + 1] - '0'
					: ch[i * 2 + 1] + 10 - 'A');
			c = (char) ((ch[i * 2]) << 4);
			c += (char) (ch[i * 2 + 1]);
			out[i] = c;
		}
		return out;
	}

	/**
	 * 将不可见字符转为2个可见字符(char)
	 * 
	 * @return
	 */
	public static String hextostr(char[] b) {
		char c[] = new char[b.length * 2];
		char yan_L = 0x0f;
		char yan_H = 0xf0;
		for (int i = 0; i < b.length; i++) {
			int h = (b[i] & yan_H) >> 4;
			int l = b[i] & yan_L;
			c[i * 2] = (char) (h < 10 ? h + '0' : (h - 10) + 'A');
			c[i * 2 + 1] = (char) (l < 10 ? l + '0' : (l - 10) + 'A');
		}
		String out = new String(c);
		return out;
	}

	public static void main(String sss[]) {
		// System.out.println(new String(
		// new byte[] { 0x74, 0x65, 0x30, 0x36, 0x4f }));
		// System.out
		// .println(new String(
		// hextobin("65737431323036454E63317377457801404142434445464748492")));
		// NwList.getNwThread(new String(new byte[] {
		// (byte) ((int) (Math.random() * 10) + 0x30),
		// (byte) ((int) Math.random() * 10 + 0x30),
		// (byte) ((int) Math.random() * 10 + 0x30),
		// (byte) ((int) Math.random() * 10 + 0x30) }));
		String s;
		long b = System.currentTimeMillis(), e;
		for (int i = 0; i < 1000000; i++) {
			hextobin(new String(new byte[] {
					(byte) ((int) (Math.random() * 10) + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),

					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30),
					(byte) ((int) Math.random() * 10 + 0x30) }));
		}
		e = System.currentTimeMillis();
		System.out.println("Date:" + (e - b));
	}
	
	public static byte[] intToByteArray1(int i) {   
		  byte[] result = new byte[4];   
		  result[0] = (byte)((i >> 24) & 0xFF);
		  result[1] = (byte)((i >> 16) & 0xFF);
		  result[2] = (byte)((i >> 8) & 0xFF); 
		  result[3] = (byte)(i & 0xFF);
		  return result;
		 }
	
	public static int byteArrayToInt(byte[] b, int offset) {
	       int value= 0;
	       for (int i = 0; i < 4; i++) {
	           int shift= (4 - 1 - i) * 8;
	           value +=(b[i + offset] & 0x000000FF) << shift;
	       }
	       return value;
	 }
}
