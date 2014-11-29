package com.larcloud.action;

import com.larcloud.model.BaseModel;
import com.larcloud.service.VideoService;
import com.larcloud.util.ParamMap;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-6-17
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */

@Controller
@Namespace(value = "/video")
public class VideoAction extends BaseAction {
    private Logger logger = Logger.getLogger(VideoAction.class.getName());
    @Resource(name = "VideoServiceImpl")
    VideoService mainService;

    @Action(value = "add")
    public void add() {
        params.put(BaseModel.CREATED_AT,new Date());
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
    public void list() {

        Object resp=mainService.list(params);
        writer.writeOK(resp);
    }


}
