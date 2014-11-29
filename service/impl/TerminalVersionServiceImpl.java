package com.larcloud.service.impl;

import com.larcloud.model.ListModel;
import com.larcloud.model.TerminalVersion;
import com.larcloud.dao.postgresql.TerminalVersionDao;
import com.larcloud.service.TerminalVersionService;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-3-4
 * Time: 下午12:58
 * To change this template use File | Settings | File Templates.
 */
@Service("TerminalVersionServiceImpl")
public class TerminalVersionServiceImpl implements TerminalVersionService{

    @Autowired
    TerminalVersionDao versionDao;


    @Override
    public ParamMap add(ParamMap params) {
        versionDao.add(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        versionDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        versionDao.update(params);
    }

    @Override
    public TerminalVersion get(ParamMap params) {
        return versionDao.get(params);
    }

    @Override
    public ListModel<TerminalVersion> list(ParamMap params) {
        ListModel<TerminalVersion> listModel = new ListModel<TerminalVersion>();
        listModel.setData(versionDao.list(params));
        listModel.setTotalCount(versionDao.count(params));
        listModel.setPageNumber(params.getInteger(ListModel.PAGE_NUMBER));
        listModel.setPageSize(params.getInteger(ListModel.PAGE_SIZE));
        return listModel;
    }

    @Override
    public Integer count(ParamMap params) {
        return versionDao.count(params);
    }
}
