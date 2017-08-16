package com.yanbao.service;

import com.yanbao.core.service.CommonService;
import com.mall.model.UserAddress;

/**
 * Created by summer on 2016-12-12:17:01;
 */
public interface UserAddressService extends CommonService<UserAddress> {

    void updateByUserId(UserAddress userAddress);
}
