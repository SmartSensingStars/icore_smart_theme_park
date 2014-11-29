package com.larcloud.service.impl;

import com.larcloud.dao.postgresql.OrderDao;
import com.larcloud.dao.postgresql.OrderItemDao;
import com.larcloud.model.ListModel;
import com.larcloud.model.Order;
import com.larcloud.model.OrderItem;
import com.larcloud.service.OrderItemService;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-28
 * Time: 下午5:07
 * To change this template use File | Settings | File Templates.
 */
@Service("OrderItemServiceImpl")
public class OrderItemServiceImpl implements OrderItemService {
    private Logger logger = Logger.getLogger(OrderItemServiceImpl.class.getName());
    @Autowired
    OrderItemDao mainDao;



    @Override
    public ParamMap add(ParamMap params) {
        mainDao.add(params);
        return params;
    }

    @Override
    public void delete(ParamMap params) {
        mainDao.delete(params);
    }

    @Override
    public void update(ParamMap params) {
        mainDao.update(params);
    }

    @Override
    public OrderItem get(ParamMap params) {
        return mainDao.get(params);
    }

    @Override
    public ListModel<OrderItem> list(ParamMap params) {
        ListModel<OrderItem> list = new ListModel<OrderItem>();
        List<OrderItem> listData=mainDao.list(params);
        list.setData(listData);
        list.setTotalCount(mainDao.count(params));
        list.setPageNumber(params.getInteger(ListModel.PAGE_NUMBER));
        list.setPageSize(params.getInteger(ListModel.PAGE_SIZE));
        return list;
    }

    @Override
    public Integer count(ParamMap params) {
        return mainDao.count(params);
    }

    @Override
    public void deleteMany(ParamMap params) {
        List<Double> orderItemIdList=(List<Double>)params.get(OrderItem.ORDER_ITEM_ID_LIST);
        for(Double _orderItemId: orderItemIdList){
            Integer orderItemId= _orderItemId.intValue();
            mainDao.delete(new ParamMap(new ParamItem(OrderItem.ORDER_ITEM_ID,orderItemId)));
        }
    }
}
