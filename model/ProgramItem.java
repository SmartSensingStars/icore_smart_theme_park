package com.larcloud.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-1
 * Time: 下午12:17
 * To change this template use File | Settings | File Templates.
 */
public class ProgramItem extends BaseModel {
    public static final String PROGRAM_ITEM_ID = "programItemId";
    public static final String TERMINAL_NAME="terminalName";
    public static final String STATUS = "status";
    public static final Integer STATUS_START = 1;
    public static final Integer STATUS_OK = 8;
    public static final Integer STATUS_ERROR = 4;
    public static final String STATUS_STRING = "statusString";
    public static final String FILE_LOCATION = "fileLocation";
    public static final String TAG_NO = "tagNo";

    private Integer programItemId;
    private Program program;
    private Integer cameraId;
    private Camera camera;
    private Terminal terminal;
    private Integer status;
    private String statusString;
    private String fileLocation;
    private String tagNo;
    private Video video;
    private Date endAt;
    private Date startAt;


    public Integer getProgramItemId() {
        return programItemId;
    }

    public void setProgramItemId(Integer programItemId) {
        this.programItemId = programItemId;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (status==STATUS_START){
            statusString="进行";
        }
        if (status==STATUS_OK){
            statusString="完成";
            this.fileLocation = "20140501/c"+cameraId+"-"+programItemId+".mp4";
        }
        if (status==STATUS_ERROR){
            statusString="错误";
        }
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation() {
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }


    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }


}
