package com.larcloud.message;

import java.util.Date;

public class BaseMessage {

	private String status;
	private String message;
	private String code;
	private Object data;
	private Date serverTime;

	public Object getData() {
		return data;
	}

	public Date getServerTime() {
		return serverTime;
	}

	public void setServerTime(Date serverTime) {
		this.serverTime = serverTime;
	}


	public void setData(Object pData) {
		data = pData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
