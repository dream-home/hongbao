package com.yanbao.dao.impl;

import com.mall.model.OrderTypeModel;
import com.yanbao.dao.OrderTypeDao;
import com.yanbao.mapper.OrderTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @date 2018年08月29日
 */
@Repository
public class OrderTypeImpl implements OrderTypeDao {

    @Autowired
    private OrderTypeMapper orderTypeMapper;

    @Override
    public OrderTypeModel getById(String id) {
        return orderTypeMapper.getById(id);
    }

    @Override
    public void add(OrderTypeModel model) {
        orderTypeMapper.add(model);
    }
}
