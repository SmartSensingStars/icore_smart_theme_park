package com.larcloud.model;

import com.larcloud.util.DateUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-18
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public class Video extends BaseModel {
    public static final String VIDEO_ID = "videoId";
    public static final String FILE_PATH = "filePath";
    public static final String STATUS = "status";
    public static final Integer STATUS_START = 1;
    public static final Integer STATUS_OK = 8;
    public static final Integer STATUS_ERROR = 4;
    private Integer videoId;
    private String filePath;
    private Integer cameraId;
    private Camera camera;
    private Integer status;
    private String statusName;

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;

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
        if(status == STATUS_START){
            statusName = "进行";
        }else if (status == STATUS_OK){
            statusName = "完成";
        }else{
            statusName = "错误";
        }

//        if (status==STATUS_OK){
//            this.filePath =  generateFilePath();
//        }
    }

    public String generateFilePath(){
        return DateUtil.dateToStringShort(new Date())+"/C"+cameraId+"-V"+videoId+".flv";
    }

    public String getStatusName() {
        return statusName;
    }
}
