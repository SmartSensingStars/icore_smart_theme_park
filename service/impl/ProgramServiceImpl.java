package com.larcloud.service.impl;

import com.larcloud.dao.postgresql.ProgramDao;
import com.larcloud.dao.postgresql.RoleDao;
import com.larcloud.model.ListModel;
import com.larcloud.model.Program;
import com.larcloud.model.Role;
import com.larcloud.service.ProgramService;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-30
 * Time: 下午5:51
 * To change this template use File | Settings | File Templates.
 */
@Service("ProgramServiceImpl")
public class ProgramServiceImpl implements ProgramService {
    @Autowired
    ProgramDao mainDao;

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
    public Program get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<Program> list(ParamMap params) {
        ListModel<Program> list = new ListModel<Program>();
        List<Program> listData=mainDao.list(params);
        /*for (Program program:listData){
            program.setStatusString();
        }*/
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
