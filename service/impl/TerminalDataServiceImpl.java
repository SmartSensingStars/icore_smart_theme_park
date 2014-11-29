package com.larcloud.service.impl;

import com.larcloud.model.BaseModel;
import com.larcloud.model.ListModel;
import com.larcloud.model.TerminalData;
import com.larcloud.dao.postgresql.TerminalDataDao;
import com.larcloud.service.TerminalDataService;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-4
 * Time: 下午1:55
 * To change this template use File | Settings | File Templates.
 */
@Service("TerminalDataServiceImpl")
public class TerminalDataServiceImpl implements TerminalDataService {
    @Autowired
    TerminalDataDao terminalDataDao;

    @Override
    public ParamMap add(ParamMap params) {
        params.put(BaseModel.CREATED_AT,new Date());
        terminalDataDao.add(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        terminalDataDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        terminalDataDao.update(params);
    }

    @Override
    public TerminalData get(ParamMap params) {
        return terminalDataDao.get(params);
    }

    @Override
    public ListModel<TerminalData> list(ParamMap params) {
        ListModel<TerminalData> list = new ListModel<TerminalData>();
        list.setData(terminalDataDao.list(params));
        list.setTotalCount(terminalDataDao.count(params));
        list.setPageNumber(params.getInteger(ListModel.PAGE_NUMBER));
        list.setPageSize(params.getInteger(ListModel.PAGE_SIZE));
        return list;
    }

    @Override
    public Integer count(ParamMap params) {
        return terminalDataDao.count(params);
    }

    @Override
    public Double sum(ParamMap params) {
        return terminalDataDao.sum(params);
    }
}
