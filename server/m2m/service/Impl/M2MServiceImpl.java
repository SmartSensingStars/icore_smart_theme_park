package com.larcloud.server.m2m.service.Impl;

//import com.larcloud.work.nwwork.M2MObject;
import com.larcloud.component.ITerminalComponent;
import com.larcloud.model.Terminal;
import com.larcloud.model.TerminalEvent;
import com.larcloud.server.m2m.message.M2MDataJsonMessage;
import com.larcloud.server.m2m.message.M2MMessage;
import com.larcloud.server.m2m.service.IM2MService;
import com.larcloud.service.TerminalEventService;
import com.larcloud.service.TerminalService;
import com.larcloud.service.TerminalVersionService;
import com.larcloud.util.*;
//import com.larcloud.work.CloudServer;
//import com.larcloud.work.NwList;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-3
 * Time: 上午11:39
 * To change this template use File | Settings | File Templates.
 */
//@Service("M2MServiceImpl")
//兼容鹏讯协议的实现
public class M2MServiceImpl implements IM2MService{
    private Logger logger = Logger.getLogger(M2MServiceImpl.class.getName());
    @Autowired
    ITerminalComponent comp;

    @Resource(name="TerminalServiceImpl")
    TerminalService terminalService;

    @Resource(name="TerminalEventServiceImpl")
    TerminalEventService terminalEventService;

    @Resource(name="TerminalVersionServiceImpl")
    TerminalVersionService terminalVersionService;

//    private M2MMessage convertGlodioM2M(M2MObject mo) {
//        M2MMessage msg=new M2MMessage();
//        msg.setCommandId(mo.getCommid());
//        msg.setMessageServerId(mo.getCloudid());
//        try{
//            msg.setSerialNo(new String(mo.getNw_id(),"UTF-8"));
//        }catch(UnsupportedEncodingException ex){
//            logger.error(StringUtil.getFullStackTrace(ex));
//        }
//        msg.setTxNo(ByteUtil.getIntegerFromBytes(mo.getB_seria()));
//        msg.setBody(mo.getOutmsg());
//        return msg;
//    }

    @Override
    public void receive(M2MMessage m2mMessage) {
        byte[] commandId=m2mMessage.getCommandId();
        if (commandId[0]==0x00 && commandId[1]==0x10){
            //this is a json message from terminal
            String json="";
            try {
                json=new String(m2mMessage.getBody(),"UTF-8");


                ParamMap params=ParamMap.fromJson(json);
                Integer typeId= params.getInteger(M2MDataJsonMessage.TYPE_ID);
                if (typeId==M2MDataJsonMessage.TYPE_T2T){
                    String toSerialNo=params.getString("to");
                    params.put("from",m2mMessage.getSerialNo());
                    M2MMessage msg=new M2MMessage();
                    msg.setSerialNo(toSerialNo);
                    msg.setCommandId(M2MMessage.COMMAND_DATA_JSON_DOWN);
                    msg.setData(params);
                    send(msg);
                    sendAckOk(m2mMessage);                }
            }
            catch(Exception ex){
                logger.error(StringUtil.getFullStackTrace(ex));
                //reply 8010 now we only reply error ack
                sendAckError(m2mMessage, ex.getMessage());
            }
        }

    }

    @Override
    public void sendThrough(M2MMessage m2mMessage) throws  Exception {
//        try {
//            String serialNo=m2mMessage.getSerialNo();
//            M2MObject m2mObject = new M2MObject();
//            m2mObject.setCommid(m2mMessage.getCommandId());
//            m2mObject.setNw_id(serialNo.getBytes());
//            if(m2mMessage.getTxNo()==-1){
//                m2mObject.setSeria(comp.increaseTxNo(serialNo));
//            }else{
//                m2mObject.setSeria(m2mMessage.getTxNo());
//            }
//
//            m2mObject.setOutmsg(m2mMessage.getBody());
//
//            CloudServer.sendToTerminalByTcp(m2mObject);
//
//        } catch (Exception e) {
//            logger.error(StringUtil.getFullStackTrace(e));
//            throw e;
//        }
    }

    @Override
    public void send(M2MMessage m2mMessage) throws Exception {
        try {
            if (m2mMessage.getBody()==null && m2mMessage.getData()!=null){
                m2mMessage.setBody(m2mMessage.getData().toJson().getBytes("UTF-8"));
            }
            sendThrough(m2mMessage);
        } catch (Exception e) {
            logger.error(StringUtil.getFullStackTrace(e));
            throw e;
        }
    }

    @Override
    public void sendAckOk(M2MMessage m2mMessage){
        M2MMessage okAck=new M2MMessage();
        okAck.setSerialNo(m2mMessage.getSerialNo());
        okAck.setTxNo(m2mMessage.getTxNo());
        okAck.setCommandId(M2MMessage.COMMAND_DATA_JSON_UP_ACK);
        ParamMap params=new ParamMap(
            new ParamItem(M2MDataJsonMessage.TYPE_ID,M2MDataJsonMessage.TYPE_ACK_OK)
        );
        okAck.setData(params);
        try{
           send(okAck);
        }
        catch (Exception ex){
            logger.error(StringUtil.getFullStackTrace(ex));
        }
    }

    @Override
    public void sendAckError(M2MMessage m2mMessage, String errorString){
        M2MMessage errorAck=new M2MMessage();
        errorAck.setSerialNo(m2mMessage.getSerialNo());
        errorAck.setTxNo(m2mMessage.getTxNo());
        errorAck.setCommandId(M2MMessage.COMMAND_DATA_JSON_UP_ACK);
        ParamMap params=new ParamMap(
                new ParamItem(M2MDataJsonMessage.TYPE_ID,M2MDataJsonMessage.TYPE_ACK_ERROR)
        );
        errorAck.setData(params);
        try {
            send(errorAck);
        } catch (Exception e) {
            logger.error(StringUtil.getFullStackTrace(e));
        }
    }

//    @Override
//    public void dispatchGlodioM2M(M2MObject mo) {
//        M2MMessage msg=convertGlodioM2M(mo);
//        byte[] commandId = msg.getCommandId();
//        // 0001
//        if (commandId[0] == 0x00 && commandId[1] == 0x01) {
//            // 登录消息
//            login(msg);
//        }
//
//        // 0002
//        if (commandId[0] == 0x00 && commandId[1] == 0x02) {
//            // 退出消息
//            logout(msg);
//        }
//
//        //json message
//        if (commandId[0] == (byte)0x00 && commandId[1] == 0x10) {
//            receive(msg);
//        }
//
//       /* // 0x0003
//        if (b[0] == 0x00 && b[1] == 0x03) {
//            // 应用层心跳
//            CloudHandleInterface.dtuHreat(mo);
//        }
//
//        // 0x0007
//        if (b[0] == 0x00 && b[1] == 0x07) {
//            // Trap 故障上报
//            CloudHandleInterface.configTrap(mo);
//
//        }
//
//        // 8006
//        if (b[0] == (byte)0x80 && b[1] == 0x06) {
//            // CONFIG_SET 配置ACK
//            CloudHandleInterface.configSet_ACK(mo);
//            // 设置ACK消息流水状态
//            MsgStatList.pushMsg(new String(mo.getNw_id()) + mo.getSeria(), 4);
//        }
//
//        // 801C
//        if (b[0] == (byte)0x80 && b[1] == 0x1C) {
//            // 远程升级 ACK 1
//            CloudHandleInterface.rometeUp_ACKp1(mo);
//            // 设置ACK消息流水状态
//            MsgStatList.pushMsg(new String(mo.getNw_id()) + mo.getSeria(), 4);
//        }
//
//        // 800C
//        if (b[0] == (byte)0x80 && b[1] == 0x0C) {
//            // 远程升级 ACK 2
//            CloudHandleInterface.rometeUp_ACKp2(mo);
//            // 设置ACK消息流水状态
//            MsgStatList.pushMsg(new String(mo.getNw_id()) + mo.getSeria(), 4);
//        }
//*/
//        //other command id  routing to IM2MService
//
//    }

    @Override
    public void login(M2MMessage m2mMessage) {
//        try{
//
//            String serialNo=m2mMessage.getSerialNo();
//            logger.info("Terminal-["+serialNo+ "] is trying to login in.");
//            byte body[] = new byte[5];
//
//            Terminal terminal=terminalService.get(new ParamMap(new ParamItem(Terminal.SERIAL_NO,serialNo)));
//            if (terminal == null) {
//                // 说明终端不存在,返回非法code
//                body[0] = 0x01;
//                logger.error("Terminal-["+serialNo + "] is not registered!");
//            } else {
//                Integer onlineStatus=terminal.getOnlineStatus();
//                if (onlineStatus==Terminal.ONLINE_STATUS_OFFLINE) {
//                    // 正常登录
//                    body[0] = 0x0;
//                } else {
//                    // 已登录终端
//                    body[0] = 0x06;
//                }
//                //record login time
//                terminalService.update(new ParamMap(
//                    new ParamItem(Terminal.LAST_ONLINE_AT,new Date()),
//                    new ParamItem(Terminal.ONLINE_STATUS,Terminal.ONLINE_STATUS_ONLINE) ,
//                    new ParamItem(Terminal.TERMINAL_ID,terminal.getTerminalId())
//                ));
//
//                ParamMap paramsTerminalEvent=new ParamMap();
//                paramsTerminalEvent.put(Terminal.TERMINAL_ID,terminal.getTerminalId());
//                paramsTerminalEvent.put(TerminalEvent.TYPE_ID, TerminalEvent.TYPE_ONLINE);
//                paramsTerminalEvent.put(TerminalEvent.CREATED_AT,new Date());
//                terminalEventService.add(paramsTerminalEvent);
//                //check login body for tlv groups, 1. 3001 2.0011 3. E021
//                byte[] m2mBody=m2mMessage.getBody();
//                ByteArrayInputStream is = new ByteArrayInputStream(m2mBody);
//                is.read(new byte[32],0,32);
//                byte[] tag=new byte[2];
//                is.read(tag,0,2);
//                if (StringUtil.bytesToHex(tag).equals("3001")){
//                    byte[] length=new byte[2];
//                    is.read(length,0,2);
//                    int intLength= TlvCodec.bytes2int(length);
//                    byte[] terminalVersion=new byte[intLength];
//                    is.read(terminalVersion,0,intLength);
//
//                   /* String terminalVersionStr = new String(terminalVersion ,"UTF-8" );
//                    ParamMap paramsTerminalVersion=new ParamMap();
//                    paramsTerminalVersion.put(TerminalVersion.CODE,terminalVersionStr);
//                    TerminalVersion version = terminalVersionService.get(paramsTerminalVersion);
//                    if(version!= null){
//                        ParamMap paramsInfo=new ParamMap(new ParamItem(Terminal.SERIAL_NO,nw));
//                        NwInfo nwInfo=nws.getNwInfo(paramsInfo);
//                        if(nwInfo.getNwTypeId() != version.getTerminalVersionId()){
//                            ParamMap paramsTerminalupdate=new ParamMap();
//                            paramsTerminalupdate.put(TerminalVersion.TERMINAL_VERSION_ID,version.getTerminalVersionId());
//                            paramsTerminalupdate.put(Terminal.SERIAL_NO,nw);
//                            nws.update(paramsTerminalupdate);
//                        }
//                    }*/
//                }
//
//                byte[] tagIpPort=new byte[2];
//                is.read(tagIpPort, 0, 2);
//                if (StringUtil.bytesToHex(tagIpPort).equals("0011")){
//                    byte[] lengthIpPort=new byte[2];
//                    is.read(lengthIpPort,0,2);
//                    int intLengthIpPort= TlvCodec.bytes2int(lengthIpPort);
//                    byte[] ipPort=new byte[intLengthIpPort];
//                    is.read(ipPort,0,intLengthIpPort);
//                    String strIpPort=(new String(ipPort)).trim();
//                    String[] ipParts=strIpPort.split(":");
//
//                    if (ipParts.length==2 ){
//                        //save ip and port. read it first, if changed then update db
//                        if (!(terminal.getIpAddress()+":"+terminal.getPort()).equals(strIpPort)){
//                            ParamMap paramsIpPort=new ParamMap();
//                            paramsIpPort.put(Terminal.TERMINAL_ID, terminal.getTerminalId());
//                            paramsIpPort.put(Terminal.IP_ADDRESS, ipParts[0]);
//                            paramsIpPort.put(Terminal.PORT, ipParts[1]);
//                            paramsIpPort.put(Terminal.UPDATED_AT, new Date());
//                            terminalService.update(paramsIpPort);
//                        }
//                    }
//                }
//
//                NwList.addOrUPNwPlat(serialNo, m2mMessage.getMessageServerId());
//            }
//
//            //send login ack
//            m2mMessage.setCommandId(M2MMessage.COMMAND_LOGIN_ACK);
//            m2mMessage.setBody(body);
//            sendThrough(m2mMessage);
//
//            logger.debug("Terminal-["+serialNo + "] logged in ok!");
//
//        }catch (Exception e) {
//            logger.error(ExceptionUtils.getFullStackTrace(e));
//        }
    }

    @Override
    public void logout(M2MMessage m2mMessage) {
//        try{
//
//            String serialNo = m2mMessage.getSerialNo();
//            logger.info("Terminal-[" + serialNo + "] is trying to logout.");
//            // backbody
//            byte body[] = new byte[5];
//
//            Terminal terminal=terminalService.get(new ParamMap(new ParamItem(Terminal.SERIAL_NO,serialNo)));
//            if (terminal == null) {
//                // 无法退出,无法找到将要退出的终端
//                body[0] = 0x01;
//                logger.error("Terminal-[" + serialNo + "] logout fail , not exists");
//
//            } else {
//                Integer onlineStatus=terminal.getOnlineStatus();
//                if (onlineStatus == Terminal.ONLINE_STATUS_OFFLINE) {
//                    // 已退出的终端
//                    body[0] = 0x02;
//                } else {
//                    // 正常退出
//                    body[0] = 0x00;
//
//                    ParamMap params=new ParamMap();
//                    params.put(Terminal.SERIAL_NO,serialNo);
//                    params.put(Terminal.LAST_OFFLINE_AT,new Date());
//                    params.put(Terminal.ONLINE_STATUS,Terminal.ONLINE_STATUS_OFFLINE);
//                    terminalService.update(params);
//                    //update event
//
//                    ParamMap paramsTerminalEvent=new ParamMap();
//                    paramsTerminalEvent.put(Terminal.TERMINAL_ID,terminal.getTerminalId());
//                    paramsTerminalEvent.put(TerminalEvent.TYPE_ID, TerminalEvent.TYPE_OFFLINE);
//                    paramsTerminalEvent.put(TerminalEvent.CREATED_AT,new Date());
//                    terminalEventService.add(paramsTerminalEvent);
//                }
//
//
//            }
//
//            //send logout ack
//            m2mMessage.setCommandId(M2MMessage.COMMAND_LOGOUT_ACK);
//            m2mMessage.setBody(body);
//            sendThrough(m2mMessage);
//
//            if (NwList.upNw(serialNo) != null) {
//                // 更新终端的链路信息
//                // -0已下线
//                NwList.upNw(serialNo).setNwstat(0);
//
//            }
//
//            logger.debug("Terminal-["+serialNo + "] logged out ok!");
//        }catch (Exception e) {
//            logger.error(ExceptionUtils.getFullStackTrace(e));
//        }
    }
}
