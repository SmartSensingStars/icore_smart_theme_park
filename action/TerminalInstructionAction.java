package com.larcloud.action;

import com.larcloud.model.TerminalInstruction;
import com.larcloud.service.TerminalInstructionService;
import com.larcloud.service.TerminalService;
import com.larcloud.util.ParamMap;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-2
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/terminalInstruction")
public class TerminalInstructionAction extends BaseAction {
    @Resource(name = "TerminalInstructionServiceImpl")
    TerminalInstructionService mainService;

    @Resource(name = "TerminalServiceImpl")
    TerminalService terminalService;



    @Action(value = "add")
    public void add() {
       ParamMap newObj=mainService.add(params);

        writer.writeOK(newObj);
    }

    @Action(value = "delete")
    public void delete() {

        mainService.delete(params);
        writer.writeOK();
    }

    @Action(value = "update")
    public void update() {
        mainService.update(params);

        writer.writeOK();
    }

    @Action(value = "get")
    public void get()
    {
        writer.writeOK(mainService.get(params));
    }

    @Action(value = "list")
    public void list()
    {


        Object resp=mainService.list(params);
        writer.writeOK(resp);
    }


}
