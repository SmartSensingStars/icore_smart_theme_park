

package com.larcloud.model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-3-7
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
public class Task extends BaseModel {
    public static final String TASK_ID = "taskId";
    public static final String NAME = "name";
    public static final String TYPE_ID = "typeId";
    public static final Integer TYPE_UPGRADE = TerminalInstruction.TYPE_UPGRADE;
    public static final Integer TYPE_CONTROL = TerminalInstruction.TYPE_CONTROL;
    public static final Integer TYPE_CONFIG = TerminalInstruction.TYPE_CONFIG;
    public static final String STATUS = "status";
    public static final String DATA = "data";
    public static final String TERMINAL_LIST_DATA = "terminalListData";
    public static final Integer STATUS_RUNNING = 0;
    public static final Integer STATUS_SUCCESS = 1;
    public static final Integer STATUS_FAIL = 4;
    public static final String TERMINAL_ID__IN = "terminalId__IN";

    private Integer taskId;
    private String name;
    private Integer status;
    private Integer typeId;
    private String data;
    private String terminalListData;
    private String statusName;
    private String typeName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTypeName() {
       return typeName ;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (status==STATUS_RUNNING){
            statusName= "正在执行";
        }
        if (status==STATUS_SUCCESS){
            statusName= "成功";
        }
        if (status==STATUS_FAIL){
            statusName= "失败";
        }
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
        if (typeId==TYPE_CONFIG){
            typeName="终端配置";
        }
        if (typeId==TYPE_UPGRADE){
            typeName= "远程升级";
        }
        if (typeId==TYPE_CONTROL){
            typeName= "终端控制";
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTerminalListData() {
        return terminalListData;
    }

    public void setTerminalListData(String terminalListData) {
        this.terminalListData = terminalListData;
    }

    public List<Integer> getTerminalIdList(){
        Gson gson = new Gson();
        Type mapType = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> list= gson.fromJson(this.getTerminalListData(),mapType);
        if (list==null){
            list= new ArrayList<Integer>();
        }
        return list;
    }
}
