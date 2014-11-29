package com.larcloud.action;

import com.larcloud.annotation.Permission;
import com.larcloud.model.BaseModel;
import com.larcloud.service.GroupService;
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
@Namespace(value = "/group")
public class GroupAction extends BaseAction {

    @Resource(name = "GroupServiceImpl")
    GroupService groupServiceImpl;

    @Action(value = "add")
    @Permission({Permission.Item.GROUP_ADD})
    public void add()
    {
        params.put(BaseModel.CREATED_AT,new Date());
        ParamMap group=groupServiceImpl.add(params);
        writer.writeOK(group);
    }

    @Action(value = "delete")
    @Permission({Permission.Item.GROUP_DELETE})
    public void delete()
    {
        groupServiceImpl.delete(params);
        writer.writeOK();
    }

    @Action(value = "update")
    @Permission({Permission.Item.GROUP_UPDATE})
    public void update()
    {

        groupServiceImpl.update(params);
        writer.writeOK();
    }

    @Action(value = "get")
    public void get()
    {
        writer.writeOK(groupServiceImpl.get(params));
    }

    @Action(value = "list")
    public void list()
    {
       /* String sortingField = (String)params.get(ListModel.SORTING_FIELD);
        String sortingDirection = (String)params.get(ListModel.SORTING_DIRECTION);
        Integer pageNumber = ((Double)params.get(ListModel.PAGE_NUMBER)).intValue();
        Integer pageSize = ((Double)params.get(ListModel.PAGE_SIZE)).intValue();*/
        Object resp=groupServiceImpl.list(params);
        writer.writeOK(resp);
    }

}
