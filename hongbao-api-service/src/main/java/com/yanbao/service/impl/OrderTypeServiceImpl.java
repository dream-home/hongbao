package com.yanbao.service.impl;

import com.mall.model.OrderTypeModel;
import com.yanbao.dao.OrderTypeDao;
import com.yanbao.service.OrderTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @date 2018年08月29日
 */
@Service
public class OrderTypeServiceImpl implements OrderTypeService {
    @Autowired
    private OrderTypeDao orderTypeDao;

    @Override
    public OrderTypeModel getById(String id) throws Exception {
        return orderTypeDao.getById(id);
    }

    @Override
    public void add(OrderTypeModel model) {
        orderTypeDao.add(model);
    }

    @Override
    public void add(String orderNo,Integer type,String remark,String token) {
        OrderTypeModel model = new OrderTypeModel();
        model.setToken(token);
        model.setId(orderNo);
        model.setCreateTime(new Date());
        model.setType(type);
        model.setRemark(remark);
        model.setStatus(1);
        orderTypeDao.add(model);
    }
}
