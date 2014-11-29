package com.larcloud.service.impl;

import com.larcloud.dao.postgresql.GroupDao;
import com.larcloud.dao.postgresql.ProgramDao;
import com.larcloud.dao.postgresql.ProgramDownloadDao;
import com.larcloud.model.ListModel;
import com.larcloud.model.Program;
import com.larcloud.model.ProgramDownload;
import com.larcloud.service.ProgramDownloadService;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-1
 * Time: 下午12:41
 * To change this template use File | Settings | File Templates.
 */
@Service("ProgramDownloadServiceImpl")
public class ProgramDownloadServiceImpl implements ProgramDownloadService {
    @Autowired
    ProgramDownloadDao mainDao;

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
    public ProgramDownload get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<ProgramDownload> list(ParamMap params) {
        ListModel<ProgramDownload> list = new ListModel<ProgramDownload>();
        List<ProgramDownload> listData=mainDao.list(params);
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
