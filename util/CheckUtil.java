package com.larcloud.util;

/**
 * 包含对数字等一些的检查
 * 
 * @author jnad
 * 
 */
public class CheckUtil {

	
	/**
	 * 检查给予的字符串是否是数组,不是则返回0 ,是则返回正确的数字
	 * @param num
	 * @return
	 */
	public static String checkNum(String num) {
		if(num!=null&&!num.equals("")){
			return num.matches("[0-9]*") ? Integer.valueOf(num).toString() : 0+"";
		}else {
			return null;
		}
		
	}

}
