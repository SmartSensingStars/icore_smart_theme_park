package com.larcloud.service.impl;

import com.larcloud.model.ListModel;
import com.larcloud.model.Terminal;
import com.larcloud.model.TerminalEvent;
import com.larcloud.dao.postgresql.TerminalDao;
import com.larcloud.dao.postgresql.TerminalEventDao;
import com.larcloud.service.TerminalEventService;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ParamMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-21
 * Time: 下午12:14
 * To change this template use File | Settings | File Templates.
 */
@Service("TerminalEventServiceImpl")
public class TerminalEventServiceImpl implements TerminalEventService {
    @Autowired
    TerminalEventDao terminalEventDao;


    @Autowired
    TerminalDao terminalDao;

    @Override
    public ParamMap add(ParamMap params) {
        terminalEventDao.add(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        terminalEventDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        terminalEventDao.update(params);
    }

    @Override
    public TerminalEvent get(ParamMap params) {
        return terminalEventDao.get(params);
    }

    @Override
    public ListModel<TerminalEvent> list(ParamMap params) {


        params.put(Terminal.TERMINAL_ID,params.getInteger(Terminal.TERMINAL_ID));
        Terminal terminal=terminalDao.get(params);
        if (terminal==null){
            throw new BaseException(BaseException.ENTITY_DOES_NOT_EXIST_ERROR);
        }
        params.put(Terminal.TERMINAL_ID,terminal.getTerminalId());
        ListModel<TerminalEvent> list = new ListModel<TerminalEvent>();
        list.setData(terminalEventDao.list(params));
        list.setTotalCount(terminalEventDao.count(params));
        list.setPageNumber(params.getInteger(ListModel.PAGE_NUMBER));
        list.setPageSize(params.getInteger(ListModel.PAGE_SIZE));
        return list;
    }

    @Override
    public Integer count(ParamMap params) {
        return terminalEventDao.count(params);
    }

}

