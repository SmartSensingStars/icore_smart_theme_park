package com.larcloud.service.impl;

import com.larcloud.dao.postgresql.ParamDao;
import com.larcloud.model.CameraVideo;
import com.larcloud.model.ListModel;
import com.larcloud.model.Param;
import com.larcloud.service.ParamService;
import com.larcloud.util.ParamMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-8-7
 * Time: 下午1:41
 * To change this template use File | Settings | File Templates.
 */
@Service("ParamServiceImpl")
public class ParamServiceImpl implements ParamService {
    private Logger logger = Logger.getLogger(ParamServiceImpl.class.getName());
    @Autowired
    ParamDao mainDao;


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
    public Param get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<Param> list(ParamMap params) {
        ListModel<Param> list = new ListModel<Param>();
        List<Param> listData=mainDao.list(params);
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
