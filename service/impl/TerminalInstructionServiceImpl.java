package com.larcloud.service.impl;

import com.larcloud.model.*;
import com.larcloud.dao.postgresql.TerminalVersionDao;
import com.larcloud.dao.postgresql.TerminalInstructionDao;
import com.larcloud.service.TerminalInstructionService;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-2
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
@Service("TerminalInstructionServiceImpl")
public class TerminalInstructionServiceImpl implements TerminalInstructionService {
    private Logger logger = Logger.getLogger(TerminalInstructionServiceImpl.class.getName());
    @Autowired
    TerminalInstructionDao mainDao;
    @Autowired
    SysConfig sysConfig;


  

    @Autowired
    TerminalVersionDao versionDao;


    @Override
    public ParamMap add(ParamMap params) {
        params.put(BaseModel.CREATED_AT,new Date());
        mainDao.add(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        mainDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {


        params.put(BaseModel.UPDATED_AT,new Date());
        mainDao.update(params);
    }

    @Override
    public TerminalInstruction get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<TerminalInstruction> list(ParamMap params) {

        ListModel<TerminalInstruction> list = new ListModel<TerminalInstruction>();
        List<TerminalInstruction> dataList= mainDao.list(params);
        /*for (TerminalInstruction ti : dataList){
            if(ti.getTypeId()== Task.TYPE_UPGRADE){
                ParamMap versionParams = new ParamMap();
                versionParams.put(TerminalVersion.TERMINAL_VERSION_ID,Integer.valueOf(ti.getDataString()));
                TerminalVersion terminalVersion = versionDao.get(versionParams);
                ti.setDataString("版本升级至"+terminalVersion.getCode());
            }else{
                ti.setDataString(ti.getDataString());
            }
            ti.setTypeName(ti.getTypeName());
            ti.setStatusName(ti.getStatusName());

        }*/
        list.setData(dataList);
        list.setTotalCount(mainDao.count(params));
        list.setPageNumber(params.getInteger(ListModel.PAGE_NUMBER));
        list.setPageSize(params.getInteger(ListModel.PAGE_SIZE));
        return list;
    }

    @Override
    public Integer count(ParamMap params) {
        return mainDao.count(params);
    }

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
}
