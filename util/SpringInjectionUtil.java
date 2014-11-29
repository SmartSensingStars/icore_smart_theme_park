package com.larcloud.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * 反反spring注入的方法,主要方便static方法保存自己的dao对象
 * 
 * @author Administrator
 * 
 */
public class SpringInjectionUtil extends ApplicationObjectSupport {

	public static Object getObjec(String beanname) {

		return null;
	}

	private void s() {
		ApplicationContext applicationContext2 = getApplicationContext();

	}
}
