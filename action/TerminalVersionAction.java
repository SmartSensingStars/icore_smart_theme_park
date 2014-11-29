package com.larcloud.action;

import com.larcloud.annotation.Permission;
import com.larcloud.model.BaseModel;
import com.larcloud.model.ListModel;
import com.larcloud.service.TerminalVersionService;
import com.larcloud.util.ParamMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-3-4
 * Time: 下午12:53
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/terminalVersion")
public class TerminalVersionAction extends BaseAction{

    @Resource(name = "TerminalVersionServiceImpl")
    TerminalVersionService mainService;

    @Action(value = "add")
    public void add()
    {
        params.put(BaseModel.CREATED_AT,new Date());
        ParamMap terminalVersion = mainService.add(params);
        writer.writeOK(terminalVersion);
    }

    @Action(value = "delete")
    public void delete()
    {
        mainService.delete(params);
        writer.writeOK();
    }

    @Action(value = "update")
    public void update()
    {
        params.put(BaseModel.UPDATED_AT,new Date());
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
