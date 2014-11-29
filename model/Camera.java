package com.larcloud.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-2
 * Time: 下午8:06
 * To change this template use File | Settings | File Templates.
 */
public class Camera extends BaseModel {
    public static final String CAMERA_ID = "cameraId";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String STATUS_NAME = "statusName";
    public static final String TYPE_ID="typeId";
    public static final String TAG_NO_LIST="tagNoList";

    public static final String PIC_ADDRESS = "picAddress";
    public static final String RECORD_STARTED_AT = "recordStartedAt";

    private Integer cameraId;
    private String name;
    private Integer status;
    private String statusName;
    private Integer typeId;
    private String typeName;
    private Terminal terminal;
    private Integer groupId;
    private Group group;
    private Date recordStartedAt;

    private CameraVideo lastCameraVideo;

    public CameraVideo getLastCameraVideo() {
        return lastCameraVideo;
    }

    public void setLastCameraVideo(CameraVideo lastCameraVideo) {
        this.lastCameraVideo = lastCameraVideo;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
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
//        if (status==0){
//            statusName="待机";
//        }
//        if (status==5){
//            statusName="工作";
//        }

        if (status==0){
            statusName="离线";
        }
        if (status==1){
            statusName="在线";
        }
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Date getRecordStartedAt() {
        return recordStartedAt;
    }

    public void setRecordStartedAt(Date recordStartedAt) {
        this.recordStartedAt = recordStartedAt;
    }
}
