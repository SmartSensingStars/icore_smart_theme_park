package com.larcloud.util;

import java.util.Date;

//import com.larcloud.work.nwwork.M2MObject;

/**
 * 提供对M2M对象的封装类
 * 
 * @author jnad
 * 
 */
public class M2MUtil {
//	/**
//	 * 根据字节数据返回一个标准M2M消息Bean
//	 *
//	 * @param b
//	 * @return
//	 */
//	public static M2MObject fromObject(byte[] b) {
//		M2MObject mo = new M2MObject();
//		mo.setMsg(b);
//		return fromObject(mo);
//
//	}
//
//	/**
//	 * 将mo.msg的消息解析后返回一个标准M2M消息
//	 *
//	 * @param mo
//	 * @return
//	 */
//	public static M2MObject fromObject(M2MObject mo) {
//		// 消息在mo.getMsg里
//		byte[] b = mo.getMsg();
//		if (b.length < 27) {
//			return null;
//		}
//		// 自动识别消息长度,并将规定长度外的消息丢弃!
//		int len = (b[0] >> 8) + b[1];
//		if (len < b.length) {
//			// 说明有些非本报文的值,截掉
//			b = ArrayCopy.subByte(b, 0, len);
//		}
//
//		// 命令ID
//		mo.setCommid(ArrayCopy.subByte(b, 2, 4));
//		// 终端编号
//		mo.setNw_id(ArrayCopy.subByte(b, 12, 28));
//		// 流水号
//		mo.setSeria(geiserianum(ArrayCopy.subByte(b, 4, 8)));
//		// 直接流水号组
//		mo.setB_seria(ArrayCopy.subByte(b, 4, 8));
//		// 协议版本
//		mo.setProver(getByteTointnum(ArrayCopy.subByte(b, 8, 10)));
//		// 云端iD (不做更改)
//		// mo.setCloudid(cloudid);
//
//		// Body消息
//		mo.setOutmsg(ArrayCopy.subByte(b, 28, b.length));
//
//		return mo;
//
//	}
//
//	public static byte[] fromHex(M2MObject mo) {
//		if (mo.getOutmsg() == null || mo.getCommid() == null
//				|| mo.getNw_id() == null) {
//			return null;
//		}
//		// 创建标准Byte[]
//		byte b[] = new byte[mo.getOutmsg().length + 28];
//		byte _t[];
//		// len
//		b[0] = (byte) ((mo.getOutmsg().length + 28) >> 8);
//		b[1] = (byte) ((mo.getOutmsg().length + 28) - (b[0] << 8));
//		// commid
//		b[2] = mo.getCommid()[0];
//		b[3] = mo.getCommid()[1];
//		// seria
//		if (mo.getB_seria() != null) {
//			// 校验是否ACK,ACK直接填写流水号
//			_t = mo.getB_seria();
//		} else {
//			// 将大数转为流水号(有问题)已更正
//			_t = getIntTobyte2(mo.getSeria());
//		}
//		b[4] = _t[0];
//		b[5] = _t[1];
//		b[6] = _t[2];
//		b[7] = _t[3];
//		// 协议版本
//		_t = getIntTobyteW2(mo.getProver());
//		b[8] = _t[0];
//		b[9] = _t[1];
//		// 安全表示
//		b[10] = (byte) mo.getSafeId();
//		// 保留字
//		b[11] = (byte) mo.getReserved();
//		System.arraycopy(mo.getNw_id(), 0, b, 12, 16);
//		System.arraycopy(mo.getOutmsg(), 0, b, 28, mo.getOutmsg().length);
//
//		return b;
//	}

//	/**
//	 * 将int值转为byte数组,并按指定长度返回(有问题,已注释)
//	 * 
//	 * @param num
//	 * @param len
//	 * @return
//	 */
//	private static byte[] getintToByte(int num, int len) {
//		byte b[] = new byte[len];
//		for (int i = len - 1, t = 0; i >= 0; i--) {
//			b[t++] = (byte) (num >> (len - i) * 8);
//		}
//		return b;
//	}
	private static byte[] getIntTobyteW2(int num) {
		byte b[] = new byte[4];
		b[0] = (byte) (num >> 8);
		b[1] = (byte) (num - (b[0] << 8));
		
		return b;
	}
	private static byte[] getIntTobyte(int num) {
		byte b[] = new byte[4];
		b[0] = (byte) (num >> 24);
		b[1] = (byte) ((num - (b[0] << 24)) >> 16);
		b[2] = (byte) ((num - (b[0] << 24) - (b[1] << 16)) >> 8);
		b[3] = (byte) (num - (b[0] << 24) - (b[1] << 16) - (b[2] << 8));
		return b;
	}

	private static byte[] getIntTobyte2(int num) {
		byte b[] = new byte[4];
		b[3] = (byte) ((num >> 0) % 0x100);
		b[2] = (byte) ((num >> 8) % 0x100);
		b[1] = (byte) ((num >> 16) % 0x100);
		b[0] = (byte) ((num >> 24) % 0x100);

		return b;
	}

	/**
	 * 将Byte的数组转为Int值
	 * 
	 * @param b
	 * @return
	 */
	private static int getByteTointnum(byte[] b) {
		int num = 0;
		for (int i = b.length - 1; i >= 0; i--) {
			num += b[i] << (b.length - i * 8);
		}
		return num;

	}

	private static int getBytebyint(byte b) {
		if (b < 0) {
			return b + 0x100;
		}

		return b;
	}

	// 返回流水号Int值
	private static int geiserianum(byte[] b) {
		return getBytebyint(b[0]) * 0x10000 + getBytebyint(b[2]) * 0x100
				+ getBytebyint(b[1]) * 0x10 + getBytebyint(b[3]);
	}


	public static void main(String s[]) {
		int num = 256;
		int x = 10000000;
		Date d = new Date();
		// [-115, 0, 0, 102]
		for (int i = x; i >= 0; i--) {
			// System.out.println("old  sum: " + geiserianum(getintToByte(num,
			// 4))
			// + "   \t " + ShowArray.showIP(getintToByte(num, 4)));
//			ShowArray.showIP(getintToByte(num, 4));
		}
		System.out.println(new Date().getTime() - d.getTime());
		d = new Date();
		for (int i = x; i >= 0; i--) {
			// System.out.println(">>   sum: " + geiserianum(getIntTobyte(num))
			// + "   \t " + ShowArray.showIP(getIntTobyte(num)));
			ShowArray.showIP(getIntTobyte(num));
		}
		System.out.println(new Date().getTime() - d.getTime());
		d = new Date();
		for (int i = x; i >= 0; i--) {
			// System.out.println("%>>  sum: " + geiserianum(getIntTobyte2(num))
			// + "   \t " + ShowArray.showIP(getIntTobyte2(num)));
			ShowArray.showIP(getIntTobyte2(num));
		}
		System.out.println(new Date().getTime() - d.getTime());
		// String str =
		// "00230044200100400101310202363003013204013705013106024472071231383A31353A3030546F32323A31343A30300816323031322D30392D3235546F323031332D30392D3236";
		//
		// System.out.println(new String(M2MUtil
		// .fromObject(CharUtil.hextobin(str)).getNw_id()));
		// System.out.println(JSONObject.fromObject(M2MUtil.fromObject(CharUtil
		// .hextobin(str))));
		// System.out.println(JSONArray.fromObject(M2MUtil.fromObject(M2MUtil
		// .fromHex(M2MUtil.fromObject(CharUtil.hextobin(str))))));

		// M2MObject mo = new M2MObject();
		// mo.setCommid(new byte[] { 00, 01 });
		// mo.setOutmsg(new byte[] { 0x01, 0x02 });
		// mo.setNw_id("test1206ENc1cwEx".getBytes());
		// System.out.println(JSONArray.fromObject(M2MUtil.fromHex(mo)));
	}
}
