package com.larcloud.service;

import com.larcloud.model.User;
import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-2-25
 * Time: 下午1:10
 * To change this template use File | Settings | File Templates.
 */
public interface UserService extends BaseService<User> {
    public User login(ParamMap params);
    public void logout();
}
