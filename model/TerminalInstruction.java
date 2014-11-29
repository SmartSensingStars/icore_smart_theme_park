package com.larcloud.model;

import com.larcloud.util.ParamMap;
import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-2
 * Time: 上午11:22
 * To change this template use File | Settings | File Templates.
 */
public class TerminalInstruction extends BaseModel {
    public static final String DB_FIELD_CREATED_AT = "terminal_instruction_created_at";

    public static final String TERMINAL_INSTRUCTION_ID = "terminalInstructionId";
    public static final String STATUS = "status";
    public static final Integer STATUS_SEND_START = 0;
    public static final Integer STATUS_SEND_OK = 1;
    public static final Integer STATUS_SEND_ERROR = 4;
    public static final Integer STATUS_ACK_RECV = 2;
    public static final Integer STATUS_ACK_ERROR =7;
    public static final Integer STATUS_ACK_OK =8;
    public static final String DATA_JSON = "dataJson";
    public static final String DATA = "data";
    public static final String TX_NO = "txNo";
    public static final String TYPE_ID = "typeId";
    public static final Integer TYPE_CONFIG = 1;
    public static final Integer TYPE_UPGRADE = 2;
    public static final Integer TYPE_CONTROL = 3;
    public static final Integer TYPE_TRANSFER = 4;
    public static final Integer TYPE_T2T = 9;
    public static final String DATA_ACTION ="action";
    public static final String DATA_ACTION_CAMERA_START ="camera/start";
    public static final String DATA_ACTION_CAMERA_STOP="camera/stop";
    public static final String DATA_CAMERA_ID__IN ="cameraId__IN";

    public static final String DATA_ACTION_TRANS_PIC_ADDRESS="transPicAddress";
    public static final String DATA_ACTION_TERMINAL_UPGRADE="terminal/upgrade";
    public static final String DATA_ACTION_TERMINAL_RECEIVE_OPEN="terminal/receiveOpen";
    public static final String DATA_ACTION_TERMINAL_RECEIVE_CLOSE="terminal/receiveClose";

    private Integer terminalInstructionId;
    private Integer typeId;
    private Integer status;
    private Integer txNo;
    private String dataJson;
    private ParamMap data;
    private Integer terminalId;
    private String typeName;
    private String statusName;
    private String dataString;

    private Terminal terminal;

    private Integer taskId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTerminalInstructionId() {
        return terminalInstructionId;
    }

    public void setTerminalInstructionId(Integer terminalInstructionId) {
        this.terminalInstructionId = terminalInstructionId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
        if (typeId==TYPE_CONFIG){
            typeName= "终端配置";
        }
        if (typeId==TYPE_UPGRADE){
            typeName= "远程升级";
        }
        if (typeId==TYPE_CONTROL){
            typeName= "终端控制";
        }
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (status== STATUS_SEND_START){
            statusName= "正在发送";
        }
        if (status== STATUS_SEND_OK){
            statusName= "发送成功";
        }
        if (status== STATUS_ACK_RECV){
            statusName="接收确认";
        }
        if (status== STATUS_SEND_ERROR){
            statusName="发送失败";
        }
        if (status== STATUS_ACK_OK){
            statusName="执行成功";
        }
        if (status== STATUS_ACK_ERROR){
            statusName="执行失败";
        }
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
        this.data=ParamMap.fromJson(dataJson);
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public String getTypeName() {

        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;

    }

    public String getStatusName() {

        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDataString() {
        String dataJson=getDataJson();
        String dataString="";
        Gson gson = new Gson();

        /*if (getTypeId()==Task.TYPE_CONFIG_SET){
            //config
            Type mapType = new TypeToken<NwConfig>() {}.getType();
            NwConfig config = gson.fromJson(dataJson, mapType);

            for (NwReport item : config.getSetList()){
                if (item.getEnable()==1){
                    String configTypeName="" ;
                    if (item.getType()==0){
                        configTypeName="信号强度";
                    }
                    if (item.getType()==1){
                        configTypeName="话费";
                    }
                    if (item.getType()==2){
                        configTypeName="流量";
                    }
                    dataString+="["+configTypeName+"]"
                            +"频率:"+item.getCollectCyc()+"s,"
                            +"周期:"+item.getCollectStartData()+"至"+item.getCollectEndData()
                            +",每日:"+item.getCollectStartDataHour()+":"+item.getCollectStartDataMinute()+"至"+item.getCollectEndDataHour()+":"+item.getCollectEndDataMinute()
                            +"</br>";
                }
            }
        }else if (getTypeId()==Task.TYPE_UPGRADE){
            Type mapType = new TypeToken<ParamMap>() {}.getType();
            ParamMap data = gson.fromJson(dataJson, mapType);
            Integer  terminalVersionId = data.getInteger(TerminalVersion.TERMINAL_VERSION_ID);
            dataString = String.valueOf(terminalVersionId);
        }*/
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
