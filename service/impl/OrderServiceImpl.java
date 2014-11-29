package com.larcloud.service.impl;

import com.larcloud.dao.postgresql.*;
import com.larcloud.model.*;
import com.larcloud.service.OrderService;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ParamMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-26
 * Time: 上午11:37
 * To change this template use File | Settings | File Templates.
 */
@Service("OrderServiceImpl")
public class OrderServiceImpl implements OrderService {
    private Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
    @Autowired
    OrderDao mainDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    ProgramItemDao programItemDao;

    @Autowired
    ProgramDownloadDao programDownloadDao;




    @Override
    public ParamMap add(ParamMap params) {
        List<String> programItemIdList=(List<String>)params.get(Order.PROGRAM_ITEM_ID_LIST);
        int count = programItemIdList.size();
        if(count > 1){
            String cardNum = "";
            for(int i = 0 ; i < count; i++){
                ParamMap programItemParams = new ParamMap();
                programItemParams.put(ProgramItem.PROGRAM_ITEM_ID,Integer.valueOf(programItemIdList.get(i)));
                if(i == 0){
                    cardNum = programItemDao.get(programItemParams).getTagNo();
                }else{
                    if(cardNum.equals(programItemDao.get(programItemParams).getTagNo())){
                        continue;
                    }else{
                        throw new BaseException(BaseException.PARAMETER_ERROR);
                    }
                }
            }
        }
        params.put(BaseModel.CREATED_BY,User.getLoggedInUser().getUserId());
        mainDao.add(params);
        Integer orderId=params.getInteger(Order.ORDER_ID);
        for(int i = 0 ; i < count; i++){
            ParamMap orderItemParams = new ParamMap();
//            orderItemParams.put(OrderItem.ORDER_ITEM_ID,orderId);
            orderItemParams.put(OrderItem.PRODUCT_ID,Integer.valueOf(programItemIdList.get(i)));
            orderItemParams.put(BaseModel.CREATED_BY,User.getLoggedInUser().getUserId());
            orderItemParams.put(BaseModel.CREATED_AT,new Date());
            orderItemParams.put(Order.ORDER_ID,orderId);

            orderItemDao.add(orderItemParams);
        }

        ParamMap programDownloadParams = new ParamMap();
        programDownloadParams.put(ProgramDownload.ORDER_ID,orderId);
        programDownloadParams.put(BaseModel.CREATED_BY,User.getLoggedInUser().getUserId());
        programDownloadParams.put(BaseModel.CREATED_AT,new Date());
        programDownloadDao.add(programDownloadParams);

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
    public Order get(ParamMap params) {
//        params.put(BaseModel.CREATED_BY,User.getLoggedInUser().getUserId());
        return mainDao.get(params);
    }

    @Override
    public ListModel<Order> list(ParamMap params) {
        ListModel<Order> list = new ListModel<Order>();
        List<Order> listData=mainDao.list(params);
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



}
