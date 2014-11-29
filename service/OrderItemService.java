package com.larcloud.service;

import com.larcloud.model.OrderItem;
import com.larcloud.util.ParamMap;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-28
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
public interface OrderItemService extends BaseService<OrderItem> {
    public void deleteMany(ParamMap params);

}
