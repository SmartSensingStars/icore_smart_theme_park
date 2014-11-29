package com.larcloud.service;

import com.larcloud.model.TerminalData;
import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-4
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public interface TerminalDataService extends BaseService<TerminalData> {
    Double sum(ParamMap params);
}
