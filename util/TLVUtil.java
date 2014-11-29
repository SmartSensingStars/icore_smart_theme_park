package com.larcloud.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 提供对TLV的解压和封装(过时方法,等待重构)
 * 
 * @author jnad
 * 
 */
public class TLVUtil {

	/**
	 * 根据传进Map 返回标准3层tlv结果字符
	 * 
	 * @return
	 */
	public synchronized final static  String checkTLVs_Lv3(Map<String, String> amap) {
		/**
		 * 3层Tlv 结构Map
		 */
		Map<String, Map<String, Map<String, String>>> inMap;

		inMap = new HashMap<String, Map<String, Map<String, String>>>();
		Iterator<String> it = amap.keySet().iterator();
		while (it.hasNext()) {
			String tag = it.next();
			String t1 = tag.substring(2, 6);
			String t2 = tag.substring(6, 10);
			String t3 = tag.substring(10, 12);
			String val = amap.get(tag);
			saveInmap(inMap, t1, t2, t3, val);
		}

		// 开始组装
		Iterator<String> it1 = inMap.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		while (it1.hasNext()) {
			String t1_key = it1.next();
			Map<String, Map<String, String>> t2 = inMap.get(t1_key);

			// 开始载入第一次Tag 并开始记录其偏移地址
			sb.append(CharUtil.strtohex(t1_key));
			// 记录当前的偏移地址
			int p1 = sb.length();
			Iterator<String> it2 = t2.keySet().iterator();
			while (it2.hasNext()) {
				String t2_key = it2.next();
				Map<String, String> t3 = inMap.get(t1_key).get(t2_key);
				// 开始载入第二层Tag 并开始记录其偏移地址
				sb.append(CharUtil.strtohex(t2_key));
				// 记录当前的偏移地址
				int p2 = sb.length();

				Iterator<String> it3 = t3.keySet().iterator();
				while (it3.hasNext()) {
					// 扒干净了
					String t3_key = it3.next();
					String t3_val = inMap.get(t1_key).get(t2_key).get(t3_key);
					// 校验Value长度是否超出
					if (t3_val.length() > 255) {
						t3_val = t3_val.substring(0, 255);
					}
					int t3_len = t3_val.toCharArray().length;
					// 3层组装 ---
					// T
					sb.append(CharUtil.strtohex(t3_key));
					// L
					sb.append((char) t3_len);
					// V
					sb.append(t3_val.toCharArray());
				}
				// 其长度则是 当前长度减去记录点
				sb.insert(p2, TLVUtil.tlvLen(sb.length(), p2));
			}

			// 处理1级长度
			sb.insert(p1, TLVUtil.tlvLen(sb.length(), p1));

		}

		return sb.toString();
	}

	/**
	 * 根据记录点编号,及当前值 返回char 数组长度
	 * 
	 * @param len
	 *            当前总长度
	 * @param p
	 *            记录点
	 * @return
	 */
	private static char[] tlvLen(int all_len, int p) {
		char lenchar[] = new char[2];
		int len = all_len - p;
		if (len < 256) {
			lenchar[0] = 0;
			lenchar[1] = (char) len;
		} else {
			lenchar[0] = (char) (len / 256);
			lenchar[1] = (char) (len % 256);
		}
		return lenchar;

	}

	/**
	 * 保存排序数据 , 主要功能排序 ,过滤重复
	 * 
	 * @param t1
	 * @param t2
	 * @param t3
	 * @param val
	 */
	private static void saveInmap(
			Map<String, Map<String, Map<String, String>>> inMap, String t1,
			String t2, String t3, String val) {
		// 初始化t1
		if (inMap.get(t1) == null) {
			Map<String, Map<String, String>> t_t1 = new HashMap<String, Map<String, String>>();
			inMap.put(t1, t_t1);
		}
		if (inMap.get(t1).get(t2) == null) {
			Map<String, String> t_t2 = new HashMap<String, String>();
			inMap.get(t1).put(t2, t_t2);
		}
		// 3层直接写里面 ,不做处理
		if (inMap.get(t1).get(t2).get(t3) == null) {

		}
		inMap.get(t1).get(t2).put(t3, val);

	}

	/**
	 * 处理3层TLV 报文
	 * 
	 * @param amap
	 * @return
	 */
	public static Map<String, String> uncheckT3(String bodyc) {
		Map<String, String> outmap = new HashMap<String, String>();

		char body_c[] = CharUtil.strtohex(bodyc);
		if (!(body_c[0] == 0)) {
			// 返回结果是不对的
			outmap.put("stat", "7");
			return outmap;
		}
		char body[] = subArray(body_c, 1, body_c.length);
		System.out.println(CharUtil.hextostr(body));
		// m1
		Map<String, String> t1map = checkTLVs_p2(body);
		Iterator<String> it1 = t1map.keySet().iterator();
		while (it1.hasNext()) {
			String t1 = it1.next();
			Map<String, String> t2map = checkTLVs_p2(CharUtil.strtohex(t1map
					.get(t1)));
			Iterator<String> it2 = t2map.keySet().iterator();
			while (it2.hasNext()) {
				String t2 = it2.next();
				Map<String, String> t3map = checkTLVs_p1(CharUtil
						.strtohex(t2map.get(t2)));
				Iterator<String> it3 = t3map.keySet().iterator();
				while (it3.hasNext()) {
					String t3 = it3.next();
					String val = t3map.get(t3);
					// 扒干净了 开始 组装
					String alltag = "0x" + t1 + t2 + t3;
					outmap.put(alltag, new String(CharUtil.strtohex(val)));
				}

			}

		}
		System.out.println(JSONObject.fromObject(outmap));
		outmap.put("stat", "0");
		return outmap;
	}

	/**
	 * 将tlv数组转化为map格式 (2位tag)抛弃 lenth长度
	 * 
	 * @param tlv
	 * @return
	 */
	public static Map<String, String> checkTLVs_p2(char tlv[]) {
		Map<String, String> amap = new HashMap<String, String>();
		// 长度
		int len = tlv.length;
		boolean iswhile = true;
		// 偏移点..
		int p = 0;

		while (iswhile) {
			// 开始解释..
			String tag = CharUtil.hextostr(subArray(tlv, p + 0, p + 2));
			int length = (int) tlv[p + 2] * 0x100 + tlv[p + 3];
			String val = CharUtil
					.hextostr(subArray(tlv, p + 4, p + 4 + length));
			// 添加到 map里
			amap.put(tag, val);
			System.out.println("[M2MObject] tag [T2V2]: " + tag + "   val : "
					+ val);
			p += 4 + length;
			if (p >= len - 4) {
				iswhile = false;
			}
		}

		return amap;
	}

	/**
	 * 将tlv数组转化为map格式 (1位tag)抛弃 lenth长度
	 * 
	 * @param tlv
	 * @return
	 */
	public static Map<String, String> checkTLVs_p1(char tlv[]) {
		Map<String, String> amap = new HashMap<String, String>();
		// 长度
		int len = tlv.length;
		boolean iswhile = true;
		// 偏移点..
		int p = 0;

		while (iswhile) {
			// 开始解释..
			String tag = CharUtil.hextostr(subArray(tlv, p + 0, p + 1));
			int length = (int) tlv[p + 1];
			String val = CharUtil
					.hextostr(subArray(tlv, p + 2, p + 2 + length));
			// 添加到 map里
			amap.put(tag, val);
			System.out.println("[M2MObject] tag [T1V1]: " + tag + "   val : "
					+ val);
			p += 2 + length;
			if (p >= len - 2) {
				iswhile = false;
			}
		}

		return amap;
	}

	/**
	 * 将数组进行截断
	 * 
	 * @return
	 */
	public static char[] subArray(char[] c, int begin, int end) {
		char[] out = new char[end - begin];

		for (int i = 0; begin < end; begin++) {
			out[i] = c[begin];
			i++;
		}
		return out;
	}
}
