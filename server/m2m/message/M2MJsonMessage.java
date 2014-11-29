package com.larcloud.server.m2m.message;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-7
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class M2MJsonMessage extends M2MMessage {
    public M2MJsonMessage(){
        this.setCommandId(COMMAND_DATA_JSON_DOWN);
    }
}
