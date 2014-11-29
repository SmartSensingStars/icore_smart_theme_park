package com.larcloud.action;

import com.larcloud.annotation.Permission;
import com.larcloud.model.BaseModel;
import com.larcloud.service.ProgramService;
import com.larcloud.service.UserService;
import com.larcloud.util.ParamMap;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-29
 * Time: 下午8:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/program")
public class ProgramAction extends BaseAction {
    private Logger logger = Logger.getLogger(ProgramAction.class.getName());
    @Resource(name = "ProgramServiceImpl")
    ProgramService mainService;

    @Action(value = "add")
    //@Permission({Permission.Item.USER_ADD})
    public void add() {
        params.put(BaseModel.CREATED_AT,new Date());
        ParamMap newObj=mainService.add(params);
        writer.writeOK(newObj);
    }

    @Action(value = "delete")
    @Permission({Permission.Item.PROGRAM_DELETE})
    public void delete() {

        mainService.delete(params);
        writer.writeOK();
    }
    @Action(value = "update")
    @Permission({Permission.Item.PROGRAM_UPDATE})
    public void update() {
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
