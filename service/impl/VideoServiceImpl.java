package com.larcloud.service.impl;

import com.larcloud.component.IInstructionComponent;
import com.larcloud.dao.postgresql.TerminalDataDao;
import com.larcloud.dao.postgresql.VideoDao;
import com.larcloud.dao.postgresql.ProgramItemDao;
import com.larcloud.model.TerminalData;
import com.larcloud.model.Video;
import com.larcloud.model.ListModel;
import com.larcloud.model.Video;
import com.larcloud.service.VideoService;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import com.larcloud.util.StringUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-18
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
@Service("VideoServiceImpl")
public class VideoServiceImpl implements VideoService {
    private Logger logger = Logger.getLogger(VideoServiceImpl.class.getName());
    @Autowired
    VideoDao mainDao;

    @Autowired
    TerminalDataDao terminalDataDao;


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
    }

    @Override
    public Video get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<Video> list(ParamMap params) {
        ListModel<Video> list = new ListModel<Video>();
        List<Video> listData=mainDao.list(params);
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


}
