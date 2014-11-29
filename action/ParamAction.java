package com.larcloud.action;

import com.larcloud.annotation.Permission;
import com.larcloud.model.BaseModel;
import com.larcloud.model.User;
import com.larcloud.service.CameraService;
import com.larcloud.service.ParamService;
import com.larcloud.util.ParamMap;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-8-7
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/param")
public class ParamAction extends BaseAction {
    private Logger logger = Logger.getLogger(ParamAction.class.getName());
    @Resource(name = "ParamServiceImpl")
    ParamService mainService;

    @Action(value = "add")
    @Permission({Permission.Item.USER_ADD})
    public void add() {
        params.put(BaseModel.CREATED_AT,new Date());
        params.put(BaseModel.CREATED_BY, User.getLoggedInUser().getUserId());
        ParamMap newObj=mainService.add(params);
        writer.writeOK(newObj);
    }

    @Action(value = "delete")
    @Permission({Permission.Item.PROGRAM_ITEM_DELETE})
    public void delete() {

        mainService.delete(params);
        writer.writeOK();
    }
    @Action(value = "update")
    @Permission({Permission.Item.PROGRAM_ITEM_UPDATE})
    public void update() {
        params.put(BaseModel.UPDATED_AT,new Date());
        params.put(BaseModel.UPDATED_BY, User.getLoggedInUser().getUserId());
        mainService.update(params);
        writer.writeOK();
    }

    @Action(value = "get")
    public void get()
    {
        writer.writeOK(mainService.get(params));
    }

    @Action(value = "list")
    public void list() {

        Object resp=mainService.list(params);
        writer.writeOK(resp);
    }

}
