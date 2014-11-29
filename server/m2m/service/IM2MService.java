package com.larcloud.server.m2m.service;

//import com.larcloud.work.nwwork.M2MObject;
import com.larcloud.server.m2m.message.M2MMessage;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-3
 * Time: 上午11:18
 * To change this template use File | Settings | File Templates.
 */
public interface IM2MService {
    public void receive(M2MMessage m2mMessage);
    public void sendThrough(M2MMessage m2MMessage) throws Exception;
    public void send(M2MMessage m2MMessage) throws Exception;
    public void sendAckOk(M2MMessage m2mMessage);
    public void sendAckError(M2MMessage m2mMessage,String errorString);
//    public void dispatchGlodioM2M(M2MObject mo);
    public void login(M2MMessage m2mMessage);
    public void logout(M2MMessage m2mMessage);
}
