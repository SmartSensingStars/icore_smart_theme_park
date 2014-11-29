package com.larcloud.action;

import com.larcloud.model.BaseModel;
import com.larcloud.service.TerminalService;
import com.larcloud.util.ParamMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 13-6-8
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/terminal")
public class TerminalAction extends BaseAction {

    @Resource(name = "TerminalServiceImpl")
    TerminalService terminalServiceImpl;

    @Action(value = "add")
    public void add()
    {
        params.put(BaseModel.CREATED_AT,new Date());
        ParamMap newObj=terminalServiceImpl.add(params);
        writer.writeOK(newObj);
    }

    @Action(value = "delete")
    public void delete()
    {
        terminalServiceImpl.delete(params);
        writer.writeOK();
    }

    @Action(value = "deleteMany")
    public void deleteMany()
    {
        terminalServiceImpl.deleteMany(params);
        writer.writeOK();
    }

    @Action(value = "update")
    public void update()
    {
        params.put(BaseModel.UPDATED_AT,new Date());
        terminalServiceImpl.update(params);
        writer.writeOK();
    }

    @Action(value = "updateVersionMany")
    public void updateVersionMany()
    {
        params.put(BaseModel.UPDATED_AT,new Date());
        terminalServiceImpl.updateVersionMany(params);
        writer.writeOK();
    }

    @Action(value = "get")
    public void get()
    {
        writer.writeOK(terminalServiceImpl.get(params));
    }

    @Action(value = "list")
    public void list()
    {
        /* String sortingField = (String)params.get(ListModel.SORTING_FIELD);
     String sortingDirection = (String)params.get(ListModel.SORTING_DIRECTION);
     Integer pageNumber = ((Double)params.get(ListModel.PAGE_NUMBER)).intValue();
     Integer pageSize = ((Double)params.get(ListModel.PAGE_SIZE)).intValue();*/
        Object resp=terminalServiceImpl.list(params);
        writer.writeOK(resp);
    }

    @Action(value = "sendToTerminal")
    public void sendToTerminal()
    {
        terminalServiceImpl.sendToTerminal(params);
        writer.writeOK();
    }

    @Action(value = "m2m")
    public void m2m()
    {
        ParamMap paramMap=terminalServiceImpl.m2m(params);
        writer.writeOK(paramMap);
    }
}

