package com.larcloud.service.impl;

import com.larcloud.async.AsyncRequestItem;
import com.larcloud.async.AsyncRequestManager;
import com.larcloud.component.IInstructionComponent;
import com.larcloud.dao.postgresql.CameraDao;
import com.larcloud.dao.postgresql.ProgramItemDao;
import com.larcloud.dao.postgresql.TaskDao;
import com.larcloud.dao.postgresql.TerminalDao;
import com.larcloud.model.*;
import com.larcloud.service.ProgramItemService;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import com.larcloud.util.StringUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Service("ProgramItemServiceImpl")
public class ProgramItemServiceImpl implements ProgramItemService {
    private Logger logger = Logger.getLogger(ProgramItemServiceImpl.class.getName());
    @Autowired
    ProgramItemDao mainDao;

    @Autowired
    TerminalDao termialDao;

    @Autowired
    CameraDao cameraDao;

    @Autowired
    TaskDao taskDao;

    @Autowired
    AsyncRequestManager asyncRequestManager;


    @Override
    public ParamMap add(ParamMap params) {
        int cameraId =params.getInteger(Camera.CAMERA_ID);
        Integer terminalId = cameraDao.get(new ParamMap(new ParamItem(Camera.CAMERA_ID,cameraId))).getTerminal().getTerminalId();
//        Long startAt = Long.parseLong(params.get(CameraVideo.START_AT).toString());
//        Timestamp ts =  new Timestamp(startAt);
//        params.put(CameraVideo.START_AT,ts);
        params.put(CameraVideo.START_AT,new Date());
        params.put(Terminal.TERMINAL_ID,terminalId);
        params.put(Terminal.CREATED_AT,new Date());
        mainDao.add(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        mainDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        int cameraId =params.getInteger(Camera.CAMERA_ID);
        Integer terminalId = cameraDao.get(new ParamMap(new ParamItem(Camera.CAMERA_ID,cameraId))).getTerminal().getTerminalId();
        params.put(Terminal.TERMINAL_ID,terminalId);
        Integer status=params.getInteger(ProgramItem.STATUS);
        if (status != null && status==ProgramItem.STATUS_OK){
//            Long endAt = Long.parseLong(params.get(CameraVideo.END_AT).toString());
//            Timestamp ts =  new Timestamp(endAt);
//            params.put(CameraVideo.END_AT,ts);
            params.put(CameraVideo.END_AT,new Date());
        }
        params.put(Terminal.UPDATED_AT,new Date());

        ProgramItem programItem = mainDao.get(params);
        params.put(ProgramItem.PROGRAM_ITEM_ID,programItem.getProgramItemId());

        mainDao.update(params);
    }

    @Override
    public ProgramItem get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<ProgramItem> list(ParamMap params) {
        ListModel<ProgramItem> list = new ListModel<ProgramItem>();
        List<ProgramItem> listData=mainDao.list(params);

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
    public String play(ParamMap params) throws IOException,InterruptedException{
        Integer programItemId=params.getInteger(ProgramItem.PROGRAM_ITEM_ID);
        ProgramItem programItem=mainDao.get(
            new ParamMap(
                    new ParamItem(ProgramItem.PROGRAM_ITEM_ID,programItemId)
            )
        );
//        Long videoStartTick=programItem.getCreatedAt().getTime();
//        Long videoEndTick=programItem.getEndedAt().getTime();
        Long videoStartTick=programItem.getStartAt().getTime();
        Long videoEndTick=programItem.getEndAt().getTime();


        String videoFileName="V"+programItem.getProgramItemId()+".mp4";

        String liveFilePath="C:\\Users\\spectrext\\work\\apache-tomcat-7.0.37-windows-x64\\apache-tomcat-7.0.37\\webapps\\video\\live.mp4";
        String videoDirectory="C:\\Users\\spectrext\\work\\apache-tomcat-7.0.37-windows-x64\\apache-tomcat-7.0.37\\webapps\\video\\";

        Date liveStartDate=cameraDao.get(new ParamMap(
                new ParamItem(Camera.CAMERA_ID,1)
        )).getRecordStartedAt();

       /* try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            liveStartDate = formatter.parse("2014-07-28 16:11:44");

        } catch (ParseException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
*/


        Long liveStartTick=liveStartDate.getTime();
        Integer start= new Long((videoStartTick-liveStartTick)/1000).intValue();
        Integer start1=start % 60;
        Integer start2=start-start1;
        Integer duration=new Long((videoEndTick-videoStartTick)/1000).intValue();;
        //String catalinaHome=System.getProperty("catalina.home")+ File.separator+"webapps"+File.separator+"video"+File.separator;
        //String fileName="v"+start+duration+".flv";
        //String fileName="test.mp4";
        String cmd="ffmpeg -y -ss "+start+" -i "+liveFilePath+"  -t "+duration+" -c copy "+videoDirectory+videoFileName;
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

        HttpServletRequest request= ServletActionContext.getRequest();
        String url=request.getRequestURL().toString().split(request.getRequestURI())[0];
        return url+"/video/"+videoFileName;
    }


    @Override
    public void startTestCamera(ParamMap params) throws IOException,InterruptedException{
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                String liveFilePath="C:\\Users\\spectrext\\work\\apache-tomcat-7.0.37-windows-x64\\apache-tomcat-7.0.37\\webapps\\video\\live.mp4";
                String cmd="ffmpeg -y -i http://192.168.1.22:8090/video.flv -c copy -movflags frag_keyframe "+liveFilePath;



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
                }catch (IOException ex){
                    logger.error(StringUtil.getFullStackTrace(ex));
                }catch (InterruptedException ex){
                    logger.error(StringUtil.getFullStackTrace(ex));
                }
            }
        }).start();
        cameraDao.update(new ParamMap(
                new ParamItem(Camera.CAMERA_ID,1),
                new ParamItem(Camera.RECORD_STARTED_AT,new Date())
        ));
    }

}
