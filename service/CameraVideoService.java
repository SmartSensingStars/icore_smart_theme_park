package com.larcloud.service;

import com.larcloud.model.CameraVideo;
import com.larcloud.util.ParamMap;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-7-30
 * Time: 下午1:06
 * To change this template use File | Settings | File Templates.
 */
public interface CameraVideoService extends BaseService<CameraVideo> {
    public String compositeVideo(ParamMap params) throws IOException,InterruptedException;        //合成视频

}
