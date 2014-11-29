package com.larcloud.server.m2m.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.larcloud.model.Terminal;
import com.larcloud.util.ParamMap;
import com.larcloud.util.StringUtil;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-3
 * Time: 下午12:09
 * To change this template use File | Settings | File Templates.
 */
public class M2MMessage {
    private static Logger logger = Logger.getLogger(M2MMessage.class.getName());
    public static final String COMMAND_ID ="commandId";
    public static final byte[] COMMAND_LOGIN=new byte[]{0x00,0x01};
    public static final byte[] COMMAND_LOGIN_ACK = new byte[] { (byte) 0x80, 0x01 };
    public static final byte[] COMMAND_LOGOUT=new byte[]{0x00,0x02};
    public static final byte[] COMMAND_LOGOUT_ACK  = new byte[] { (byte) 0x80, 0x02 };

    public static final byte[] COMMAND_DATA_JSON_UP=new byte[]{0x00,0x10};
    public static final byte[] COMMAND_DATA_JSON_DOWN=new byte[]{0x00,0x11};
    public static final byte[] COMMAND_DATA_JSON_UP_ACK=new byte[]{(byte)0x80,0x10};
    public static final byte[] COMMAND_DATA_JSON_DOWN_ACK=new byte[]{(byte)0x80,0x11};

    public static final byte[] COMMAND_P2P=new byte[]{0x30,0x01};

    public static final byte[] COMMAND_UDP_ACK=new byte[]{(byte)0x90,0x08};
    public static final byte[] COMMAND_UDP_ACK_ERROR=new byte[]{(byte)0x90,0x04};

    public static final String SERIAL_NO = "serialNo";
    public static final String TX_NO = "txNo";
    public static final String BODY ="body";
    public static final String DATA ="data";
    public static final String MESSAGE_SERVER_ID = "messageServerId";

    private Integer messageServerId;
    private byte[] commandId;
    private String serialNo;
    private Integer txNo=-1;
    private byte[] body;
    private ParamMap data;
    private Object dataObj;

    public Integer getMessageServerId() {
        return messageServerId;
    }

    public void setMessageServerId(Integer messageServerId) {
        this.messageServerId = messageServerId;
    }

    public byte[] getCommandId() {
        return commandId;
    }

    public void setCommandId(byte[] commandId) {
        this.commandId = commandId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getTxNo() {
        return txNo;
    }

    public void setTxNo(Integer txNo) {
        this.txNo = txNo;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public ParamMap getData() {
        return data;
    }

    public void setData(ParamMap data) {
        this.data = data;
    }

    public Object getDataObj() {
        return dataObj;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(dataObj);
        try{
            this.body= json.getBytes("UTF-8");
        }catch (UnsupportedEncodingException ex){
            logger.error(StringUtil.getFullStackTrace(ex));
        }
    }

    public byte[] toByteArray() {
        //TODO: 一般消息的处理，需要处理udp流水号，异或码

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        DataOutputStream dos=new DataOutputStream(baos);

        Integer length=body.length+28;
        try {
            dos.writeShort(length);
            dos.write(commandId);
            dos.writeInt(txNo);
            dos.writeShort(0);
            dos.writeByte(0);
            dos.writeByte(0);
            dos.write(serialNo.getBytes("UTF-8"));
            dos.write(body);
        } catch (IOException e) {

        }finally {
            return baos.toByteArray();
        }
    }

    public static M2MMessage fromByteArray(byte[] buffer) throws IOException{
        ByteArrayInputStream bais=new ByteArrayInputStream(buffer);
        DataInputStream dis=new DataInputStream(bais);

        Integer length=dis.readUnsignedShort();
        byte[] commandId=new byte[2];
        dis.read(commandId);

        Integer txNo=dis.readInt();
        dis.skipBytes(4);

        byte[] serialNoByteArray=new byte[16];
        dis.read(serialNoByteArray);
        String serialNo=new String(serialNoByteArray,"UTF-8");

        byte[] body=new byte[length-28];
        dis.read(body);



        M2MMessage m2m=new M2MMessage();
        m2m.setBody(body);
        m2m.setSerialNo(serialNo);
        m2m.setCommandId(commandId);
        m2m.setTxNo(txNo);
        if (m2m.isJsonData() || m2m.isP2P()) {
            try {
                m2m.setData(ParamMap.fromJson(new String(body,"UTF-8")));
            } catch (UnsupportedEncodingException e) {
                logger.error(StringUtil.getFullStackTrace(e));
            }
        }

        return m2m;
    }

    public Boolean isLogin(){
        return (getCommandId()[0]==COMMAND_LOGIN[0]) && (getCommandId()[1]==COMMAND_LOGIN[1]);
    }

    public Boolean isP2P(){
        return (getCommandId()[0]==COMMAND_P2P[0]) && (getCommandId()[1]==COMMAND_P2P[1]);
    }

    public Boolean isJsonData(){
        return (getCommandId()[0]==COMMAND_DATA_JSON_UP[0]) && (getCommandId()[1]==COMMAND_DATA_JSON_UP[1]);
    }

    public ParamMap toParamMap(){
        ParamMap paramMap=new ParamMap();
        paramMap.put(Terminal.SERIAL_NO,serialNo);
        paramMap.put(Terminal.TX_NO,txNo);
        paramMap.put(COMMAND_ID,StringUtil.bytesToHex(commandId));

        if (isJsonData()){
            paramMap.put(DATA,data);
        }else{
            paramMap.put(BODY,StringUtil.bytesToHex(body));
        }


        return paramMap;
    }

    public static M2MMessage fromParamMap(ParamMap paramMap){
        M2MMessage m2m=new M2MMessage();
        m2m.setSerialNo(paramMap.getString(Terminal.SERIAL_NO));
        m2m.setTxNo(paramMap.getInteger(Terminal.TX_NO));
        m2m.setCommandId(StringUtil.hexStringToBytes(paramMap.getString(COMMAND_ID)));
        ParamMap data=(ParamMap)paramMap.get(DATA);
        if (data==null){
            m2m.setBody(StringUtil.hexStringToBytes(paramMap.getString(BODY)));
        }else{
            m2m.setData(data);
        }
        return m2m;
    }

    public String toJson(){
        return toParamMap().toJson();
    }

    public static M2MMessage fromJson(String json){
        return M2MMessage.fromParamMap(ParamMap.fromJson(json));
    }
}
