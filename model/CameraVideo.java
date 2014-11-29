package com.larcloud.model;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-7-30
 * Time: 下午1:07
 * To change this template use File | Settings | File Templates.
 */
public class CameraVideo extends BaseModel {
    public static final String CAMERA_VIDEO_ID = "cameraVideoId";
    public static final String FILE_PATH = "filePath";
    public static final String START_AT = "startAt";         //开始时间
    public static final String END_AT = "endAt";         //结束时间
    public static final String PROGRAM_ITEM_LIST = "programItemList";

    private Integer cameraVideoId;
    private String filePath;
    private Date startAt;
    private Date endAt;
    private Integer cameraId;
    private Camera camera;
    private List<ProgramItem> programItemList;

    private Long relativeStart;
    private Long duringTime;
    private Long startTime;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public List<ProgramItem> getProgramItemList() {
        return programItemList;
    }

    public void setProgramItemList(List<ProgramItem> programItemList) {
        this.programItemList = programItemList;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Integer getCameraVideoId() {
        return cameraVideoId;
    }

    public void setCameraVideoId(Integer cameraVideoId) {
        this.cameraVideoId = cameraVideoId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public Long getRelativeStart() {
        return relativeStart;
    }

    public void setRelativeStart(Long relativeStart) {
        this.relativeStart = relativeStart;
    }

    public Long getDuringTime() {
        return duringTime;
    }

    public void setDuringTime(Long duringTime) {
        this.duringTime = duringTime;
    }
}
