package com.larcloud.action;

import com.larcloud.annotation.Permission;
import com.larcloud.model.BaseModel;
import com.larcloud.model.User;
import com.larcloud.service.TaskService;
import com.larcloud.util.ParamMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-3-7
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/task")
public class TaskAction extends BaseAction{
    @Resource(name = "TaskServiceImpl")
    TaskService mainServiceImpl;

    @Action(value = "add")
    @Permission({Permission.Item.TERMINAL_UPDATE})
    public void add() {

        params.put(BaseModel.CREATED_BY, User.getLoggedInUser().getUserId());
        ParamMap newObj= mainServiceImpl.add(params);
        writer.writeOK(newObj);
    }

    @Action(value = "delete")
    public void delete() {
        mainServiceImpl.delete(params);
        writer.writeOK();
    }
    @Action(value = "update")
    public void update() {
        params.put(BaseModel.UPDATED_AT,new Date());
        mainServiceImpl.update(params);
        writer.writeOK();
    }

    @Action(value = "get")
    public void get()
    {
        writer.writeOK(mainServiceImpl.get(params));
    }

    @Action(value = "list")
    public void list()
    {
        Object resp= mainServiceImpl.list(params);
        writer.writeOK(resp);
    }

}
