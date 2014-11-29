package com.larcloud.component.Impl;

import com.larcloud.model.Group;
import com.larcloud.model.User;
import com.larcloud.component.IGroupComponent;
import com.larcloud.dao.postgresql.GroupDao;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-2
 * Time: 下午5:50
 * To change this template use File | Settings | File Templates.
 */
@Component
public class GroupComponentImpl implements IGroupComponent {
    @Autowired
    GroupDao groupDao;
    @Override
    public Boolean isAccessibleForUser(Integer groupId,User user){
        Integer userGroupId=user.getGroup().getGroupId();
        ParamMap paramsUserGroup=new ParamMap();
        paramsUserGroup.put(Group.GROUP_PARENT_ID,userGroupId);

        List<Group> userGroupList=groupDao.list(paramsUserGroup);

        Boolean isInUserGroup=false;
        for (Group group : userGroupList) {
            if (group.getGroupId()==groupId){
                isInUserGroup=true;
                break;
            }
        }
        return isInUserGroup;
    }
}
