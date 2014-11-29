package com.larcloud.task;

import com.larcloud.model.Task;
import com.larcloud.model.Terminal;
import com.larcloud.model.TerminalInstruction;
import com.larcloud.model.TerminalVersion;
import com.larcloud.server.m2m.message.M2MJsonMessage;
import com.larcloud.server.m2m.message.M2MMessage;
import com.larcloud.server.m2m.service.IM2MService;
import com.larcloud.service.TerminalVersionService;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ConfigurationSupport;
import com.larcloud.util.ParamMap;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-7
 * Time: 下午5:53
 * To change this template use File | Settings | File Templates.
 */
public class ControlTask extends BaseTask{
    private Logger logger = Logger.getLogger(ControlTask.class.getName());

    @Resource(name="M2MServiceImpl")
    IM2MService m2mService;

    @Override
    public void runForOneTerminal(Task task,Terminal terminal, ParamMap params) throws Exception{
        M2MMessage msg=new M2MMessage();
        msg.setCommandId(M2MMessage.COMMAND_DATA_JSON_DOWN);
        msg.setSerialNo(terminal.getSerialNo());
        msg.setTxNo(params.getInteger(TerminalInstruction.TX_NO));
        msg.setData(params);
        m2mService.send(msg);
    }
}
