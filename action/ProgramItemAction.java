package com.larcloud.action;

import com.larcloud.annotation.Permission;
import com.larcloud.model.BaseModel;
import com.larcloud.service.ProgramItemService;
import com.larcloud.service.ProgramService;
import com.larcloud.util.ParamMap;
import com.larcloud.util.StringUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;
import com.larcloud.util.DateUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-29
 * Time: 下午8:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/programItem")
public class ProgramItemAction extends BaseAction {
    private Logger logger = Logger.getLogger(ProgramItemAction.class.getName());
    @Resource(name = "ProgramItemServiceImpl")
    ProgramItemService mainService;

    @Action(value = "add")
    @Permission({Permission.Item.PROGRAM_ITEM_ADD})
    public void add() {
        params.put(BaseModel.CREATED_AT,new Date());
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
        mainService.update(params);
        writer.writeOK();
    }

    @Action(value = "get")
    public void get()
    {
        writer.writeOK(mainService.get(params));
    }


    @Action(value = "list")
    @Permission({Permission.Item.PROGRAM_ITEM_LIST})
    public void list() throws ParseException
    {
        setTimeLimit();
        Object resp=mainService.list(params);
        writer.writeOK(resp);
    }

    @Action(value = "play")
    public void process() throws IOException,InterruptedException{

        Object resp=mainService.play(params);
        //Object resp=mainService.play(params);
        writer.writeOK(resp);
    }

    @Action(value="startTestCamera")
    public void startTestCamera() throws IOException,InterruptedException {
        mainService.startTestCamera(params);
        //Object resp=mainService.play(params);
        writer.writeOK();
    }
}

