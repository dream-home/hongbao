package com.yanbao.service;

import com.mall.model.User;

/**
 * @date 2017年08月29日
 */
public interface ComOderService {

    Boolean handleOrder(User user,String orderNo) throws Exception;

}
