package com.larcloud.service;

import com.larcloud.model.Terminal;
import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-14
 * Time: 下午10:29
 * To change this template use File | Settings | File Templates.
 */
public interface TerminalService extends BaseService<Terminal> {
    public void clearOnlineStatus();
    public void sendToTerminal(ParamMap params);
    public ParamMap m2m(ParamMap params);
    public void deleteMany(ParamMap params);
    public void updateVersionMany(ParamMap params);

}
