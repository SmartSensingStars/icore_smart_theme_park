package com.larcloud.service.impl;

import com.larcloud.dao.postgresql.CameraDao;
import com.larcloud.model.Camera;
import com.larcloud.model.CameraGroup;
import com.larcloud.model.ListModel;
import com.larcloud.model.Terminal;
import com.larcloud.service.CameraGroupService;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-7
 * Time: 下午8:25
 * To change this template use File | Settings | File Templates.
 */
@Service("CameraGroupServiceImpl")
public class CameraGroupServiceImpl implements CameraGroupService {
    @Autowired
    CameraDao cameraDao;
    @Override
    public ParamMap add(ParamMap params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(ParamMap params) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update(ParamMap params) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CameraGroup get(ParamMap params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ListModel<CameraGroup> list(ParamMap params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Integer count(ParamMap params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void start(ParamMap params){


        List<Camera> list=cameraDao.list(new ParamMap(
           // new ParamItem(Terminal.TERMINAL_ID,terminalId)
        ));
        List<Integer> cameraIdList=new ArrayList<Integer>();
        for(Camera cam : list){
            cameraIdList.add(cam.getCameraId());
        }

    }

    @Override
    public void stop(ParamMap params){

    }
}
