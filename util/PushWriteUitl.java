package com.larcloud.util;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;

public class PushWriteUitl {
	public static final void write(String str) {
		try {
			// ServletActionContext.getResponse().setCharacterEncoding(u);
			ServletActionContext.getResponse().setContentType(
					"text/html;charset=UTF-8");
			ServletActionContext.getResponse().getWriter().write(str);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
