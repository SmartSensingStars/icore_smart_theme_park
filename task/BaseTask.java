package com.larcloud.task;

import com.larcloud.model.SysConfig;
import com.larcloud.model.Task;
import com.larcloud.model.Terminal;
import com.larcloud.model.TerminalInstruction;
import com.larcloud.service.TaskService;
import com.larcloud.service.TerminalInstructionService;
import com.larcloud.service.TerminalService;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-7
 * Time: 下午9:31
 * To change this template use File | Settings | File Templates.
 */
public class BaseTask implements Runnable {
    private Logger logger = Logger.getLogger(BaseTask.class.getName());

    @Autowired
    SysConfig sysConfig;



    @Resource(name="TerminalServiceImpl")
    private TerminalService terminalService;


    @Resource(name="TerminalInstructionServiceImpl")
    private TerminalInstructionService terminalInstructionService;

    @Resource(name="TaskServiceImpl")
    private TaskService taskService;

    private Task task;

    public  BaseTask(){

    }

    public void setTask(Task task){
        this.task=task;
    }

    @Override
    public void run() {
        for (Integer terminalId : task.getTerminalIdList()){
            ParamMap paramsTerminalInstruction=new ParamMap();
            Integer status=TerminalInstruction.STATUS_SEND_ERROR;

            Integer txNo=0;
            Terminal terminal=null;
            try{



                    terminal= terminalService.get(new ParamMap(new ParamItem(Terminal.TERMINAL_ID, terminalId)));
                    if (terminal!=null ){
                        terminalId=terminal.getTerminalId();
                        logger.debug("terminal.getTxNo() ====  " + terminal.getTxNo() + "    terminal.getSerialNo() ==== " + terminal.getSerialNo());
                        txNo=terminal.getTxNo()+1;
                        terminalService.update(new ParamMap(new ParamItem(Terminal.TERMINAL_ID,terminal.getTerminalId()),new ParamItem(Terminal.TX_NO,txNo)));

                        if (terminal.getOnlineStatus()==Terminal.ONLINE_STATUS_ONLINE){
                            if (terminalInstructionService.canSendToTerminal(new ParamMap(
                                    new ParamItem(Terminal.TERMINAL_ID,terminal.getTerminalId())
                            ))){
                                status=TerminalInstruction.STATUS_SEND_START;
                            }
                        }
                    }

                paramsTerminalInstruction.put(Terminal.TERMINAL_ID,terminalId);
                paramsTerminalInstruction.put(TerminalInstruction.STATUS,status);
                paramsTerminalInstruction.put(TerminalInstruction.TYPE_ID,task.getTypeId());
                paramsTerminalInstruction.put(TerminalInstruction.DATA_JSON,task.getData());
                paramsTerminalInstruction.put(TerminalInstruction.TX_NO,txNo);
                paramsTerminalInstruction.put(Task.TASK_ID,task.getTaskId());
                paramsTerminalInstruction.put(TerminalInstruction.CREATED_BY,task.getCreatedBy().getUserId());
                paramsTerminalInstruction=terminalInstructionService.add(paramsTerminalInstruction);

                if (status==TerminalInstruction.STATUS_SEND_START){
                    ParamMap paramsJson= ParamMap.fromJson(paramsTerminalInstruction.getString(TerminalInstruction.DATA_JSON));
                    paramsJson.put(TerminalInstruction.TERMINAL_INSTRUCTION_ID,paramsTerminalInstruction.getInteger(TerminalInstruction.TERMINAL_INSTRUCTION_ID));
                    paramsJson.put(TerminalInstruction.TX_NO,paramsTerminalInstruction.getInteger(TerminalInstruction.TX_NO));
                    this.runForOneTerminal(task, terminal,paramsJson);
                }
            }catch (Exception ex){
                logger.error(ExceptionUtils.getFullStackTrace(ex));
                Integer terminalInstructionId=paramsTerminalInstruction.getInteger(TerminalInstruction.TERMINAL_INSTRUCTION_ID);
                if (terminalInstructionId!=null){
                    terminalInstructionService.update(new ParamMap(
                        new ParamItem(TerminalInstruction.TERMINAL_INSTRUCTION_ID,terminalInstructionId),
                        new ParamItem(TerminalInstruction.STATUS,TerminalInstruction.STATUS_SEND_ERROR)));
                }else{
                    paramsTerminalInstruction.put(Terminal.TERMINAL_ID,terminalId);
                    paramsTerminalInstruction.put(TerminalInstruction.STATUS,status);
                    paramsTerminalInstruction.put(TerminalInstruction.TYPE_ID,task.getTypeId());
                    paramsTerminalInstruction.put(TerminalInstruction.DATA_JSON,task.getData());

                    paramsTerminalInstruction.put(TerminalInstruction.TX_NO,txNo);
                    paramsTerminalInstruction.put(Task.TASK_ID,task.getTaskId());
                    paramsTerminalInstruction.put(TerminalInstruction.CREATED_BY,task.getCreatedBy().getUserId());
                    terminalInstructionService.add(paramsTerminalInstruction);
                }
            }
            //send to terminal after an interval
            try {
                Thread.sleep(sysConfig.getTerminalInstructionSendInterval());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        taskService.update(new ParamMap( new ParamItem(Task.TASK_ID,task.getTaskId()), new ParamItem(Task.STATUS,Task.STATUS_SUCCESS)));
    }

    public void runForOneTerminal(Task task,Terminal terminal, ParamMap paramMap) throws Exception{

    }
}
