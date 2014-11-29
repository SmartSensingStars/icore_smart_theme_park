package com.larcloud.service;

import com.larcloud.model.Camera;
import com.larcloud.model.Task;
import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-2
 * Time: 下午8:13
 * To change this template use File | Settings | File Templates.
 */
public interface CameraService extends BaseService<Camera> {
    public void startMany(ParamMap params);
    public void stopMany(ParamMap params);
    public void openPic(ParamMap params);
}
