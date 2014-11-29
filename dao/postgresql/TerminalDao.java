package com.larcloud.dao.postgresql;

import com.larcloud.model.Terminal;
import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-14
 * Time: 下午10:28
 * To change this template use File | Settings | File Templates.
 */
public interface TerminalDao extends BaseDao<Terminal> {
    public void clearOnlineStatus();
    public void deleteMany(ParamMap params);

}
