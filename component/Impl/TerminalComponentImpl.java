package com.larcloud.component.Impl;

import com.larcloud.model.Terminal;
import com.larcloud.component.ITerminalComponent;
import com.larcloud.dao.postgresql.TerminalDao;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-23
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */
@Component
public class TerminalComponentImpl implements ITerminalComponent {
    @Autowired
    TerminalDao dao;
    @Override
    public Integer increaseTxNo(String serialNo) {
        Integer txNo=dao.get(new ParamMap(
            new ParamItem(Terminal.SERIAL_NO,serialNo)
        )).getTxNo();
        txNo++;
       dao.update(new ParamMap(
            new ParamItem(Terminal.SERIAL_NO, serialNo),
            new ParamItem(Terminal.TX_NO,txNo)
       ));
        return txNo;
    }
}
