package com.larcloud.component;

import com.larcloud.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-2
 * Time: 下午5:51
 * To change this template use File | Settings | File Templates.
 */
public interface IGroupComponent {
    public Boolean isAccessibleForUser(Integer groupId,User user);
}
