package com.yanbao.dao;

import com.mall.model.OrderTypeModel;

/**
 * @date 2018年08月29日
 */
public interface OrderTypeDao {
    OrderTypeModel getById(String id);

    void add(OrderTypeModel model);
}
