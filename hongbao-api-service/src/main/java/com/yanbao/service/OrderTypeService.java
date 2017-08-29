package com.yanbao.service;

import com.mall.model.OrderTypeModel;

/**
 * @date 2017年08月29日
 */
public interface OrderTypeService {

    OrderTypeModel getById(String id) throws Exception;

    void add(OrderTypeModel model);

    public void add(String orderNo,Integer type,String remark,String token);

}
