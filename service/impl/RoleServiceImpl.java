package com.larcloud.service.impl;

import com.larcloud.model.ListModel;
import com.larcloud.model.Role;
import com.larcloud.dao.postgresql.RoleDao;
import com.larcloud.service.RoleService;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-2-26
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
@Service("RoleServiceImpl")
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao mainDao;

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
    public Role get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<Role> list(ParamMap params) {
        ListModel<Role> list = new ListModel<Role>();
        list.setData(mainDao.list(params));
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
