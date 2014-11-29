package com.larcloud.action;

import com.larcloud.model.BaseModel;
import com.larcloud.service.TerminalEventService;
import com.larcloud.util.DateUtil;
import com.larcloud.util.ParamMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-22
 * Time: 下午6:52
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/terminalEvent")
public class TerminalEventAction extends BaseAction {
    @Resource(name = "TerminalEventServiceImpl")
    TerminalEventService terminalEventServiceImpl;

    @Action(value = "add")
    public void add()
    {
        params.put(BaseModel.CREATED_AT,new Date());
        ParamMap newObj= terminalEventServiceImpl.add(params);
        writer.writeOK(newObj);
    }

    @Action(value = "delete")
    public void delete()
    {
        terminalEventServiceImpl.delete(params);
        writer.writeOK();
    }

    @Action(value = "update")
    public void update()
    {
        params.put(BaseModel.UPDATED_AT,new Date());
        terminalEventServiceImpl.update(params);
        writer.writeOK();
    }

    @Action(value = "get")
    public void get()
    {
        writer.writeOK(terminalEventServiceImpl.get(params));
    }

    @Action(value = "list")
    public void list() throws ParseException
    {
        /* String sortingField = (String)params.get(ListModel.SORTING_FIELD);
     String sortingDirection = (String)params.get(ListModel.SORTING_DIRECTION);
     Integer pageNumber = ((Double)params.get(ListModel.PAGE_NUMBER)).intValue();
     Integer pageSize = ((Double)params.get(ListModel.PAGE_SIZE)).intValue();*/
        params.put(BaseModel.CREATED_AT_FROM, DateUtil.stringToDate((String) params.get(BaseModel.CREATED_AT_FROM)));
        Date dateTo= DateUtil.stringToDate((String) params.get(BaseModel.CREATED_AT_TO));
        Calendar c = Calendar.getInstance();
        c.setTime(dateTo);
        c.add(Calendar.DATE, 1);
        dateTo = c.getTime();
        params.put(BaseModel.CREATED_AT_TO,dateTo );
        Object resp= terminalEventServiceImpl.list(params);
        writer.writeOK(resp);
    }
}
