package com.larcloud.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.larcloud.async.AsyncRequest;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.util.Date;

public class ResponseWriter {
    private Logger logger = Logger.getLogger(ResponseWriter.class.getName());
	//public static final String u = "UTF-8";

	public void write(BaseMessage msg) {
        msg.setServerTime(new Date());
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(msg);
		writeString(json);
	}

    public void writeOK(Object responseData){
        MessageOK msg=new MessageOK();
        msg.setServerTime(new Date());
        msg.setData(responseData);
        write(msg);
    }

    public void writeOK(){
        writeOK(null);
    }
	
	private void writeString(String str) {
		try {
			//ServletActionContext.getResponse().setCharacterEncoding(u);
			ServletActionContext.getResponse().setContentType(
					"text/json;charset=UTF-8");
			ServletActionContext.getResponse().getWriter().write(str);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



    public void write(AsyncRequest ar){
        AsyncContext ac=ar.getAc();
        MessageOK msg=new MessageOK();
        msg.setServerTime(new Date());
        msg.setData(ar.getItemList());
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(msg);
        try{
            ac.getResponse().getWriter().write(json);
            ac.complete();
        }catch (IOException ex){
            logger.error(ExceptionUtils.getFullStackTrace(ex));
        }
    }
	

	
}
