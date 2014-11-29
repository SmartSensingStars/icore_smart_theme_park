package com.larcloud.service;

import com.larcloud.model.ProgramItem;
import com.larcloud.util.ParamMap;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-1
 * Time: 下午12:39
 * To change this template use File | Settings | File Templates.
 */
public interface ProgramItemService extends BaseService<ProgramItem> {
    public String play(ParamMap params) throws IOException,InterruptedException;
    public void startTestCamera(ParamMap params) throws IOException,InterruptedException;
}
