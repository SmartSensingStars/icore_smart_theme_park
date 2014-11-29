package com.larcloud.service.impl;

import com.larcloud.model.*;
import com.larcloud.dao.postgresql.TerminalVersionDao;
import com.larcloud.dao.postgresql.TaskDao;
import com.larcloud.service.TaskService;
import com.larcloud.task.ControlTask;
import com.larcloud.task.SoftwareUpgradeTask;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-3-7
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
@Service("TaskServiceImpl")
public class TaskServiceImpl implements TaskService{
    @Autowired
    @Qualifier("backendTaskExecutorService")
    ExecutorService executorService;

    @Autowired
    @Qualifier("softwareUpgradeTask")
    SoftwareUpgradeTask softwareUpgradeTask;

    @Autowired
    @Qualifier("controlTask")
    ControlTask controlTask;

    @Autowired
    TaskDao taskDao;

    @Autowired
    TerminalVersionDao terminalVersionDao;

    @Override
    public ParamMap add(ParamMap params) {
       ParamMap paramsTask=new ParamMap();

        Gson gson = new GsonBuilder().serializeNulls().create();
        paramsTask.put(Task.TERMINAL_LIST_DATA,gson.toJson(params.get(Task.TERMINAL_ID__IN)));
        paramsTask.put(BaseModel.CREATED_AT,new Date());

        Integer typeId= params.getInteger(Task.TYPE_ID);
        paramsTask.put(Task.TYPE_ID,typeId);

        paramsTask.put(Task.STATUS,Task.STATUS_RUNNING);

        if (typeId==Task.TYPE_UPGRADE){
            TerminalVersion tv=terminalVersionDao.get(new ParamMap(new ParamItem(TerminalVersion.TERMINAL_VERSION_ID, params.get(TerminalVersion.TERMINAL_VERSION_ID))));
            paramsTask.put(Task.NAME,tv.getName());
        }

        params.remove(Task.TERMINAL_ID__IN);
        paramsTask.put(Task.DATA, params.toJson());
        paramsTask.put(Task.CREATED_BY,User.getLoggedInUser().getUserId());

        taskDao.add(paramsTask);
        Task task=new Task();
        task.setTaskId(paramsTask.getInteger(Task.TASK_ID));
        task.setTypeId(paramsTask.getInteger(Task.TYPE_ID));
        task.setData(paramsTask.getString(Task.DATA));
        task.setTerminalListData(paramsTask.getString(Task.TERMINAL_LIST_DATA));
        task.setCreatedBy(User.getLoggedInUser());
        if (typeId==Task.TYPE_UPGRADE){
            softwareUpgradeTask.setTask(task);
            executorService.execute(softwareUpgradeTask);
        }
        if (typeId==Task.TYPE_CONTROL){
            controlTask.setTask(task);
            executorService.execute(controlTask);
        }
        return paramsTask;
    }

    @Override
    public void delete(ParamMap params) {
        taskDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        taskDao.update(params);
    }

    @Override
    public Task get(ParamMap params) {
        Task task = taskDao.get(params);
        task.setTypeName(task.getTypeName());
        task.setStatusName(task.getStatusName());
        return task;
    }

    @Override
    public ListModel<Task> list(ParamMap params) {
        ListModel<Task> listModel = new ListModel<Task>();
        List<Task> dataList= taskDao.list(params);

        listModel.setData(dataList);
        listModel.setTotalCount(taskDao.count(params));
        listModel.setPageNumber(params.getInteger(ListModel.PAGE_NUMBER));
        listModel.setPageSize(params.getInteger(ListModel.PAGE_SIZE));
        return listModel;
    }

    @Override
    public Integer count(ParamMap params) {
        return taskDao.count(params);
    }
}
