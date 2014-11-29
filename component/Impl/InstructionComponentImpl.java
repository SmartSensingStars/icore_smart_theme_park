package com.larcloud.component.Impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.larcloud.component.IInstructionComponent;
import com.larcloud.component.ITerminalComponent;
import com.larcloud.dao.postgresql.TerminalDao;
import com.larcloud.dao.postgresql.TerminalInstructionDao;
import com.larcloud.model.*;
import com.larcloud.server.m2m.message.M2MMessage;
import com.larcloud.server.m2m.service.IM2MService;
import com.larcloud.service.TerminalInstructionService;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import com.larcloud.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-7
 * Time: 下午5:23
 * To change this template use File | Settings | File Templates.
 */
@Component
public class InstructionComponentImpl implements IInstructionComponent{
    private Logger logger = Logger.getLogger(InstructionComponentImpl.class.getName());
    @Autowired
    TerminalInstructionDao mainDao;

    @Autowired
    TerminalDao terminalDao;

     @Autowired
    ITerminalComponent terminalCom;

    @Autowired
    SysConfig sysConfig;

    @Resource(name="M2MServiceImpl")
    IM2MService m2mService;

    @Override
    public Boolean canSendToTerminal(ParamMap params){
        params.put(ListModel.PAGE_NUMBER,1);
        params.put(ListModel.PAGE_SIZE,1);
        params.put(ListModel.SORTING_FIELD,TerminalInstruction.DB_FIELD_CREATED_AT);
        params.put(ListModel.SORTING_DIRECTION,ListModel.DESC);
        List<TerminalInstruction> list=mainDao.list(params);
        if (list.size()==0){
            //a new terminal ?
            return true;
        }
        TerminalInstruction ti=list.get(0);
        if (ti.getStatus()==TerminalInstruction.STATUS_SEND_OK || ti.getStatus()==TerminalInstruction.STATUS_SEND_ERROR){
            //yes lastest instruction was success.
            return true;
        }
        if (ti.getStatus()==TerminalInstruction.STATUS_SEND_START){
            //check time out
            Long timePassed= ((new Date()).getTime()-ti.getCreatedAt().getTime());
            if (timePassed>sysConfig.getTerminalInstructionAckTimeOut()){
                mainDao.update(new ParamMap(
                        new ParamItem(TerminalInstruction.TERMINAL_INSTRUCTION_ID,ti.getTerminalInstructionId()),
                        new ParamItem(TerminalInstruction.STATUS,TerminalInstruction.STATUS_SEND_ERROR)
                ));
                return true;
            }else{
                //still waiting for ack
                return false;
            }
        }
        if (ti.getStatus()==TerminalInstruction.STATUS_ACK_RECV){
            //only software upgrade has this
            if (ti.getTypeId()==TerminalInstruction.TYPE_UPGRADE){
                Long timePassed= ((new Date()).getTime()-ti.getCreatedAt().getTime());
                if (timePassed>sysConfig.getTerminalInstructionUpgradeTimeOut()){
                    mainDao.update(new ParamMap(
                            new ParamItem(TerminalInstruction.TERMINAL_INSTRUCTION_ID,ti.getTerminalInstructionId()),
                            new ParamItem(TerminalInstruction.STATUS,TerminalInstruction.STATUS_SEND_ERROR)
                    ));
                    return true;
                }else{
                    //still waiting for upgrade ack
                    return false;
                }
            }
        }
        //should not reach here
        logger.debug("Should not reach here!");
        return false;
    }

    @Override
    public void addAndSend(ParamMap params) {
        //add to db
        Integer terminalId=params.getInteger(Terminal.TERMINAL_ID);
        Integer typeId=params.getInteger(TerminalInstruction.TYPE_ID);
        if (canSendToTerminal(new ParamMap(new ParamItem(Terminal.TERMINAL_ID,terminalId)))){
            Terminal terminal=   terminalDao.get(new ParamMap(new ParamItem(Terminal.TERMINAL_ID,terminalId)));
            String serialNo=terminal.getSerialNo();
            Integer txNo= terminalCom.increaseTxNo(serialNo);
            M2MMessage m2m=new M2MMessage();
            m2m.setCommandId(M2MMessage.COMMAND_DATA_JSON_DOWN);
            m2m.setSerialNo(serialNo);
            m2m.setTxNo(txNo);

            params.put(TerminalInstruction.STATUS,TerminalInstruction.STATUS_SEND_START);
            params.put(TerminalInstruction.DATA_JSON,((ParamMap)params.get(TerminalInstruction.DATA)).toJson());
            params.put(BaseModel.CREATED_AT,new Date());
            params.put(BaseModel.CREATED_BY,User.getLoggedInUser().getUserId());
            params.put(TerminalInstruction.TX_NO,txNo);
            mainDao.add(params);

            try {
                m2m.setBody(params.toJson().getBytes("UTF-8"));
                m2mService.send(m2m);
            }  catch (Exception e) {
                logger.error(StringUtil.getFullStackTrace(e));
                mainDao.update(new ParamMap(
                    new ParamItem(TerminalInstruction.TERMINAL_INSTRUCTION_ID,params.getInteger(TerminalInstruction.TERMINAL_INSTRUCTION_ID)),
                    new ParamItem(TerminalInstruction.STATUS,TerminalInstruction.STATUS_SEND_ERROR)
                ));
            }

        }else{
            params.put(TerminalInstruction.STATUS,TerminalInstruction.STATUS_SEND_ERROR);
            mainDao.add(params);
        }
    }
}