package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-30
 * Time: 下午5:30
 * To change this template use File | Settings | File Templates.
 */
public class Program extends BaseModel {

    public static final String PROGRAM_ID = "programId";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String STATUS_STRING = "statusString";
    public static final String TAG_NO = "tagNo";

    private Integer programId;
    private String name;
    private Integer status;
    private String statusString;
    private String tagNo;

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
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
        if (status==1){
            statusString="进行";
        }
        if (status==5){
            statusString="处理";
        }
        if (status==8){
            statusString="完成";
        }
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }
}
