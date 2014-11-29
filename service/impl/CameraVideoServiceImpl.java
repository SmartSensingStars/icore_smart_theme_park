package com.larcloud.service.impl;

import com.larcloud.dao.postgresql.CameraDao;
import com.larcloud.dao.postgresql.CameraVideoDao;
import com.larcloud.dao.postgresql.ProgramDownloadDao;
import com.larcloud.dao.postgresql.ProgramItemDao;
import com.larcloud.model.*;
import com.larcloud.service.CameraVideoService;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import com.larcloud.util.StringUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-7-30
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
@Service("CameraVideoServiceImpl")
public class CameraVideoServiceImpl implements CameraVideoService {
    private Logger logger = Logger.getLogger(CameraVideoServiceImpl.class.getName());
    @Autowired
    CameraVideoDao mainDao;

    @Autowired
    ProgramItemDao programItemDao;

    @Autowired
    ProgramDownloadDao programDownloadDao;

    @Autowired
    CameraDao cameraDao;



    @Override
    public ParamMap add(ParamMap params) {
        String startAt =params.getString(CameraVideo.START_AT);
        Timestamp ts = Timestamp.valueOf(startAt);
        params.put(CameraVideo.START_AT,ts);
//        params.put(CameraVideo.START_AT,new Date());
        mainDao.add(params);

        params.put(Camera.STATUS,1);
        cameraDao.update(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        mainDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        CameraVideo cameraVideo = mainDao.get(params);

        String endAt =params.getString(CameraVideo.END_AT);
        Timestamp ts = Timestamp.valueOf(endAt);
        params.put(CameraVideo.CAMERA_VIDEO_ID, mainDao.get(params).getCameraVideoId());
//        params.put(CameraVideo.END_AT, ts);
        params.put(CameraVideo.END_AT, new Date());
        mainDao.update(params);

        params.put(Camera.STATUS,0);
        cameraDao.update(params);

    }

    @Override
    public CameraVideo get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<CameraVideo> list(ParamMap params) {
        ListModel<CameraVideo> list = new ListModel<CameraVideo>();
        List<CameraVideo> listData=mainDao.list(params);
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
    public String compositeVideo(ParamMap params) throws IOException,InterruptedException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat formatResult = new SimpleDateFormat("yyyyMMddHHmmss");

        String cmd = "/usr/local/bin/concat.sh ";

        ArrayList<CameraVideo> arrayList = new ArrayList<CameraVideo>();
        List<Double> programItemIdList = (List<Double>)params.get(CameraVideo.PROGRAM_ITEM_LIST);
        Integer orderId = params.getInteger(Order.ORDER_ID);

        for(Double _programItemId: programItemIdList){
            Integer programItemId= _programItemId.intValue();
            ProgramItem programItem = programItemDao.get(new ParamMap(new ParamItem(ProgramItem.PROGRAM_ITEM_ID, programItemId)));
            int cameraId = programItem.getCameraId();
            long startTime = programItem.getStartAt().getTime()/1000;
            long endTime = programItem.getEndAt().getTime()/1000;
            long duringTime  = endTime - startTime;

            CameraVideo cameraVideo = mainDao.get(new ParamMap(new ParamItem(Camera.CAMERA_ID,cameraId)));
            Long relativeStart=startTime - cameraVideo.getStartAt().getTime()/1000;
            cameraVideo.setDuringTime(duringTime);
            cameraVideo.setRelativeStart(relativeStart);
            cameraVideo.setStartTime(startTime);
            arrayList.add(cameraVideo);
        }

        Collections.sort(arrayList, new SortByStartTimeAsc());
        for(CameraVideo cameraVideo:arrayList){
            cmd = cmd + cameraVideo.getFilePath() +" " + cameraVideo.getRelativeStart() + " " + cameraVideo.getDuringTime() + " ";
        }


        String nowStr = formatResult.format(new Date(System.currentTimeMillis()));
        String returnStr = "/var/lib/tomcat7/webapps/video/" + nowStr +".mp4";
        cmd = cmd + returnStr;

        logger.debug(cmd);


        Process process;

        process = Runtime.getRuntime().exec(cmd);
        final BufferedReader in = new BufferedReader( new InputStreamReader(process.getErrorStream()));
        String line="";
        while ((line = in.readLine()) != null) {logger.debug(line);}

        process.waitFor();
        Integer ret=process.exitValue();
        logger.debug(ret);
        if (ret!=0){
            BaseException bex=new BaseException(BaseException.SYSTEM_ERROR);
            throw bex;
        }

        programDownloadDao.update(new ParamMap(new ParamItem(Order.ORDER_ID, orderId),
                new ParamItem(ProgramDownload.FILE_PATH, nowStr+".mp4"),
                new ParamItem(ProgramDownload.STATUS, ProgramDownload.STATUS_OK)));

        return nowStr+".mp4";
    }

//    @Override
//    public String compositeVideo(ParamMap params) throws IOException,InterruptedException{
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        DateFormat formatResult = new SimpleDateFormat("yyyyMMddHHmmss");
//
//        String cmd = "/usr/local/bin/concat.sh ";
//
//        ArrayList<CameraVideo> arrayList = new ArrayList<CameraVideo>();
//
//        List<HashMap> programItemList = (List<HashMap>)params.get(CameraVideo.PROGRAM_ITEM_LIST);
//        for(int i=0; i< programItemList.size(); i++ ){
//            HashMap programItem = programItemList.get(i);
//            int programItemId = ((Double)programItem.get(ProgramItem.PROGRAM_ITEM_ID)).intValue();
//            int cameraId = ((Double)programItem.get(Camera.CAMERA_ID)).intValue();
//            long startTime = 0L, endTime = 0L, duringTime;
//            try {
//                startTime = (format.parse(programItem.get(CameraVideo.START_AT).toString()).getTime())/1000;
//                endTime = (format.parse(programItem.get(CameraVideo.END_AT).toString()).getTime())/1000;
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            duringTime  = endTime - startTime;
//
//            CameraVideo cameraVideo = mainDao.get(new ParamMap(new ParamItem(Camera.CAMERA_ID,cameraId)));
//            Long relativeStart=startTime - cameraVideo.getStartAt().getTime()/1000;
//
//            cameraVideo.setDuringTime(duringTime);
//            cameraVideo.setRelativeStart(relativeStart);
//            cameraVideo.setStartTime(startTime);
//            arrayList.add(cameraVideo);
//        }
//
//        Collections.sort(arrayList, new SortByStartTimeAsc());
//        for(CameraVideo cameraVideo:arrayList){
//            cmd = cmd + cameraVideo.getFilePath() +" " + cameraVideo.getRelativeStart() + " " + cameraVideo.getDuringTime() + " ";
//        }
//
//
//        String nowStr = formatResult.format(new Date(System.currentTimeMillis()));
//        String returnStr = "/var/lib/tomcat7/webapps/video/" + nowStr +".mp4";
//        cmd = cmd + returnStr;
//
//        logger.debug(cmd);
//
//
//        Process process;
//
//        process = Runtime.getRuntime().exec(cmd);
//        final BufferedReader in = new BufferedReader( new InputStreamReader(process.getErrorStream()));
//        String line="";
//        while ((line = in.readLine()) != null) {logger.debug(line);}
//
//        process.waitFor();
//        Integer ret=process.exitValue();
//        logger.debug(ret);
//        if (ret!=0){
//            BaseException bex=new BaseException(BaseException.SYSTEM_ERROR);
//            throw bex;
//        }
//        return nowStr+".mp4";
//    }

    public class SortByStartTimeAsc implements Comparator<Object> {

        //根据CameraVideo中的startTime升序输出
        @Override
        public int compare(Object o1, Object o2) {
            CameraVideo cameraVideo1 =(CameraVideo)o1;
            CameraVideo cameraVideo2 =(CameraVideo)o2;
            if(cameraVideo1.getStartTime()>cameraVideo2.getStartTime()){
                return 1;
            }else{
                return 0;
            }
        }
    }
}
