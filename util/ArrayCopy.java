package com.larcloud.util;


public class ArrayCopy {

	public static byte[] subByte(byte[] src, int b, int e) {
		e = e - b;
		byte[] _out = new byte[e];
		for (int i = 0; i < e; i++) {
			_out[i] = src[b + i];
		}
		return _out;
	}


	public static int[] arraycopy(int[] src, int[] out) {
		if (src.length < out.length) {
			return out;
		}
		for (int i = 0; i < out.length; i++) {
			src[i] = out[i];
		}
		return src;
	}


	public static byte[] arraycopy(byte[] src, byte[] out,int b) {
		if (src.length < out.length) {
			return out;
		}
		for (int i = b; i < out.length; i++) {
			src[i] = out[i];
		}
		return src;
	}

	public static byte[] addArraySize(byte[] b, int size) {
		byte[] _b = new byte[b.length + size];
		for (int i = b.length; i > 0; i--) {
			_b[i] = b[i];
		}

		return _b;
	}

	


	public static byte[][] addArraySize(byte[][] b, int size) {
		byte[][] _b = new byte[b.length + size][];
		for (int i = b.length; i > 0; i--) {
			_b[i] = b[i];
		}

		return _b;
	}
	
	public static void main(String s[]) {

		byte b[] = new byte[] { 0, 3, 6, 4, 9, 7 };
		System.out.println(subByte(b, 5, 6)[0]);
		System.out.println("1234567".substring(1, 2));

	}
}
