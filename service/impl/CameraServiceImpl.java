package com.larcloud.service.impl;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-2
 * Time: 下午8:19
 * To change this template use File | Settings | File Templates.
 */

import com.larcloud.async.AsyncRequest;
import com.larcloud.async.AsyncRequestItem;
import com.larcloud.async.AsyncRequestManager;
import com.larcloud.component.IInstructionComponent;
import com.larcloud.dao.postgresql.*;
import com.larcloud.model.*;
import com.larcloud.service.CameraService;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import com.larcloud.util.StringUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-1
 * Time: 下午12:40
 * To change this template use File | Settings | File Templates.
 */
@Service("CameraServiceImpl")
public class CameraServiceImpl implements CameraService {
    private Logger logger = Logger.getLogger(CameraServiceImpl.class.getName());
    @Autowired
    CameraDao mainDao;

    @Autowired
    ProgramItemDao programItemDao;

    @Autowired
    IInstructionComponent instructionComponent;

    @Autowired
    VideoDao videoDao;

    @Autowired
    TaskDao taskDao;

    @Autowired
    CameraVideoDao cameraVideoDao;


    @Autowired
    AsyncRequestManager asyncRequestManager;

    @Override
    public ParamMap add(ParamMap params) {
        mainDao.add(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        mainDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        mainDao.update(params);
        Integer requestId= params.getInteger(AsyncRequestManager.REQUEST_ID);
        if (requestId!=null){
            AsyncRequestItem ari=new AsyncRequestItem();
            ari.setObjectId(params.getInteger(Camera.CAMERA_ID));
            ari.setStatus(AsyncRequestManager.STATUS_OK);
            ari.setData(params);
            asyncRequestManager.update(requestId,ari);
        }
    }

    @Override
    public Camera get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<Camera> list(ParamMap params) {
        ListModel<Camera> list = new ListModel<Camera>();
        List<Camera> listData=mainDao.list(params);
        int num = listData.size();
        for(int i = 0; i< num; i++){
           Camera camera = listData.get(i);
           CameraVideo cameraVideo = cameraVideoDao.get(new ParamMap(new ParamItem(Camera.CAMERA_ID,camera.getCameraId())));
           listData.get(i).setLastCameraVideo(cameraVideo);
        }
        list.setData(listData);
        list.setTotalCount(mainDao.count(params));
        list.setPageNumber(params.getInteger(ListModel.PAGE_NUMBER));
        list.setPageSize(params.getInteger(ListModel.PAGE_SIZE));
        return list;
    }

    @Override
    public Integer count(ParamMap params) {
        return mainDao.count(params);
    }

    @Override
    public void startMany(ParamMap params){

        List<HashMap<String,Object>> cameraList=(List<HashMap<String,Object>>)params.get("list");
        //create a task first
        ParamMap paramsTask=new ParamMap(
                new ParamItem(Task.NAME,"START CAMERA"),
                new ParamItem(Task.CREATED_BY,User.getLoggedInUser().getUserId()),
                new ParamItem(Task.STATUS,Task.STATUS_RUNNING),
                new ParamItem(Task.CREATED_AT,new Date())
        );
        taskDao.add(paramsTask);
        Integer taskId=paramsTask.getInteger(Task.TASK_ID);

        List<AsyncRequestItem> ariList=new ArrayList<AsyncRequestItem>();

        for (HashMap<String,Object> cameraParam :cameraList){
            //ParamMap cmp=(ParamMap)((HashMap<String,Object>)cameraParam);
            Integer cameraId= cameraParam.get(Camera.CAMERA_ID)==null?null:((Double)cameraParam.get(Camera.CAMERA_ID)).intValue();
            Camera camera= mainDao.get(new ParamMap(
                    new ParamItem(Camera.CAMERA_ID,cameraId)
            ));

            AsyncRequestItem ari=new AsyncRequestItem();
            ari.setObjectId(camera.getCameraId());
            ari.setStatus(AsyncRequestManager.STATUS_NEW);
            ariList.add(ari);
        }
        HttpServletRequest request= ServletActionContext.getRequest();
        asyncRequestManager.add(taskId,request, ariList);

        for (HashMap<String,Object> cameraParam :cameraList){
            Integer cameraId= cameraParam.get(Camera.CAMERA_ID)==null?null:((Double)cameraParam.get(Camera.CAMERA_ID)).intValue();
            Camera camera= mainDao.get(new ParamMap(
                    new ParamItem(Camera.CAMERA_ID,cameraId)
            ));

            ParamMap paramVideo= new ParamMap(
                new ParamItem(Camera.CAMERA_ID,camera.getCameraId()),
                new ParamItem(Video.STATUS,Video.STATUS_START),

                new ParamItem(BaseModel.CREATED_AT,new Date()),
                new ParamItem(BaseModel.CREATED_BY,User.getLoggedInUser().getUserId())
            );
            videoDao.add(paramVideo);
            Video video=new Video();
            video.setCameraId(cameraId);
            video.setVideoId(paramVideo.getInteger(Video.VIDEO_ID));
            video.setCreatedAt((Date)paramVideo.get(BaseModel.CREATED_AT));
            String videoFilePath=video.generateFilePath();
            paramVideo.put(Video.FILE_PATH, videoFilePath);
            videoDao.update(paramVideo);

            List<String> tagNoList=(List<String>) cameraParam.get(Camera.TAG_NO_LIST);

            for (String tagNo: tagNoList){
                programItemDao.add(new ParamMap(
                        new ParamItem(ProgramItem.TAG_NO,tagNo),
                        new ParamItem(Camera.CAMERA_ID,cameraId),
                        new ParamItem(BaseModel.CREATED_AT,new Date()),
                        new ParamItem(ProgramItem.STATUS,ProgramItem.STATUS_START),
                        new ParamItem(BaseModel.CREATED_BY, User.getLoggedInUser().getUserId()),
                        new ParamItem(Video.VIDEO_ID, video.getVideoId())
                ));
            }
            instructionComponent.addAndSend(new ParamMap(
                    new ParamItem(Task.TASK_ID,taskId),
                    new ParamItem(Terminal.TERMINAL_ID,camera.getTerminal().getTerminalId()),
                    new ParamItem(TerminalInstruction.TYPE_ID,TerminalInstruction.TYPE_CONTROL),
                    new ParamItem(TerminalInstruction.DATA,new ParamMap(
                            new ParamItem(TerminalInstruction.DATA_ACTION,TerminalInstruction.DATA_ACTION_CAMERA_START),
                            new ParamItem(Camera.CAMERA_ID,cameraId),
                            new ParamItem(AsyncRequestManager.REQUEST_ID,taskId),
                            new ParamItem(Video.VIDEO_ID,video.getVideoId()),
                            new ParamItem(Video.FILE_PATH,videoFilePath)
                    ))
            ));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(StringUtil.getFullStackTrace(e));
            }
        }

       /* return taskDao.get(new ParamMap(
            new ParamItem(Task.TASK_ID,taskId)
        ));*/


    }

    @Override
    public void stopMany(ParamMap params){

        List<HashMap<String,Object>> cameraList=(List<HashMap<String,Object>>)params.get("list");
        ParamMap paramsTask=new ParamMap(
                new ParamItem(Task.NAME,"STOP CAMERA"),
                new ParamItem(Task.CREATED_BY,User.getLoggedInUser().getUserId()),
                new ParamItem(Task.STATUS,Task.STATUS_RUNNING),
                new ParamItem(Task.CREATED_AT,new Date())
        );
        taskDao.add(paramsTask);

        List<AsyncRequestItem> ariList=new ArrayList<AsyncRequestItem>();


        Integer taskId=paramsTask.getInteger(Task.TASK_ID);
        for (HashMap<String,Object> cameraParam :cameraList){
            //ParamMap cmp=(ParamMap)((HashMap<String,Object>)cameraParam);
            Integer cameraId= cameraParam.get(Camera.CAMERA_ID)==null?null:((Double)cameraParam.get(Camera.CAMERA_ID)).intValue();
            Camera camera= mainDao.get(new ParamMap(
                    new ParamItem(Camera.CAMERA_ID,cameraId)
            ));

            AsyncRequestItem ari=new AsyncRequestItem();
            ari.setObjectId(camera.getCameraId());
            ari.setStatus(AsyncRequestManager.STATUS_NEW);
            ariList.add(ari);
        }
        HttpServletRequest request= ServletActionContext.getRequest();
        asyncRequestManager.add(taskId,request, ariList);


        for (HashMap<String,Object> cameraParam :cameraList){
            //ParamMap cmp=(ParamMap)((HashMap<String,Object>)cameraParam);
            Integer cameraId= cameraParam.get(Camera.CAMERA_ID)==null?null:((Double)cameraParam.get(Camera.CAMERA_ID)).intValue();
            Camera camera= mainDao.get(new ParamMap(
                    new ParamItem(Camera.CAMERA_ID,cameraId)
            ));

            instructionComponent.addAndSend(new ParamMap(
                    new ParamItem(Task.TASK_ID,taskId),
                    new ParamItem(Terminal.TERMINAL_ID,camera.getTerminal().getTerminalId()),
                    new ParamItem(TerminalInstruction.TYPE_ID,TerminalInstruction.TYPE_CONTROL),
                    new ParamItem(TerminalInstruction.DATA,new ParamMap(
                            new ParamItem(TerminalInstruction.DATA_ACTION,TerminalInstruction.DATA_ACTION_CAMERA_STOP),
                            new ParamItem(Camera.CAMERA_ID,cameraId),
                            new ParamItem(AsyncRequestManager.REQUEST_ID,taskId)
                            ))
            ));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(StringUtil.getFullStackTrace(e));
            }
        }


    }

    @Override
    public void openPic(ParamMap params){
        List<HashMap<String,Object>> cameraList=(List<HashMap<String,Object>>)params.get("list");
        for (HashMap<String,Object> cameraParam :cameraList){
            //ParamMap cmp=(ParamMap)((HashMap<String,Object>)cameraParam);
            Integer cameraId= cameraParam.get(Camera.CAMERA_ID)==null?null:((Double)cameraParam.get(Camera.CAMERA_ID)).intValue();
            Camera camera= mainDao.get(new ParamMap(
                    new ParamItem(Camera.CAMERA_ID,cameraId)
            ));
            String picAddres= cameraParam.get(Camera.PIC_ADDRESS)==null?null:cameraParam.get(Camera.PIC_ADDRESS).toString();

            instructionComponent.addAndSend(new ParamMap(
                    new ParamItem(Terminal.TERMINAL_ID,camera.getTerminal().getTerminalId()),
                    new ParamItem(TerminalInstruction.TYPE_ID,TerminalInstruction.TYPE_TRANSFER),
                    new ParamItem(TerminalInstruction.DATA,new ParamMap(
                            new ParamItem(TerminalInstruction.DATA_ACTION,TerminalInstruction.DATA_ACTION_TRANS_PIC_ADDRESS),
                            new ParamItem(Camera.CAMERA_ID,cameraId),
                            new ParamItem(Camera.PIC_ADDRESS,picAddres)
                    ))
            ));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(StringUtil.getFullStackTrace(e));
            }
        }


    }


}

