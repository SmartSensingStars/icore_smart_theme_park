package com.larcloud.service.impl;

import com.larcloud.model.Group;
import com.larcloud.model.ListModel;
import com.larcloud.model.User;
import com.larcloud.dao.postgresql.GroupDao;
import com.larcloud.dao.postgresql.UserDao;
import com.larcloud.service.UserService;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ParamMap;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-2-25
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    GroupDao groupDao;

    @Override
    public ParamMap add(ParamMap params) {
        ParamMap p=new ParamMap();
        p.put(User.USERNAME,params.getString(User.USERNAME));
        User user=userDao.get(p);
        if (user!=null){
            throw new BaseException(BaseException.USER_EXISTS);
        }
        userDao.add(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        userDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        userDao.update(params);
    }

    @Override
    public User get(ParamMap params) {
        return userDao.get(params);
    }

    @Override
    public ListModel<User> list(ParamMap params) {
        Integer groupId=params.getInteger(Group.GROUP_ID);
        if (groupId!=null){
            ParamMap paramsGroup=new ParamMap();
            paramsGroup.put(Group.GROUP_PARENT_ID,groupId);
            List<Group> groupList=groupDao.list(paramsGroup);

            ArrayList<Integer> groupIdStringList=new ArrayList<Integer>();

            for (Group group :groupList){
                groupIdStringList.add(group.getGroupId());

            }

            if (groupIdStringList.size()==0){
                groupIdStringList.add(0);
            }
            params.put(User.GROUP_ID__IN,groupIdStringList);
            params.remove(Group.GROUP_ID);
        }
        ListModel<User> list = new ListModel<User>();
        list.setData(userDao.list(params));
        list.setTotalCount(userDao.count(params));
        list.setPageNumber(params.getInteger(ListModel.PAGE_NUMBER));
        list.setPageSize(params.getInteger(ListModel.PAGE_SIZE));
        return list;
    }

    @Override
    public Integer count(ParamMap params) {
        return userDao.count(params);
    }

    @Override
    public User login(ParamMap params) {
        User loggedInUser=userDao.get(params);
        if (loggedInUser==null){
            throw new BaseException(BaseException.USER_NOT_EXISTS);
        }
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute(User.LOGGED_IN_USER, loggedInUser);
        return loggedInUser;
    }

    @Override
    public void logout(){
        ServletActionContext.getRequest().getSession().removeAttribute(User.LOGGED_IN_USER);
    }
}
