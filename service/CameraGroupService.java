package com.larcloud.service;

import com.larcloud.model.CameraGroup;
import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-7
 * Time: 下午8:24
 * To change this template use File | Settings | File Templates.
 */
public interface CameraGroupService extends BaseService<CameraGroup> {
    public void start(ParamMap params);
    public void stop(ParamMap params);
}
