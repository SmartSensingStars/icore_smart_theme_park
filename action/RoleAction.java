package com.larcloud.action;


import com.larcloud.service.RoleService;
import com.larcloud.util.ParamMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 13-6-8
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/role")
public class RoleAction extends BaseAction {

    @Resource(name = "RoleServiceImpl")
    RoleService mainServiceImpl;

    @Action(value = "add")
    public void add() {
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
