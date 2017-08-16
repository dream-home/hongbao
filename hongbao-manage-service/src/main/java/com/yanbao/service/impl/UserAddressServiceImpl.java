package com.yanbao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.UserAddressDao;
import com.mall.model.UserAddress;
import com.yanbao.service.UserAddressService;

/**
 * Created by summer on 2016-12-12:17:02;
 */
@Service
public class UserAddressServiceImpl extends CommonServiceImpl<UserAddress> implements UserAddressService{

    @Autowired
    UserAddressDao userAddressDao;

    @Override
    protected CommonDao<UserAddress> getDao() {
        return userAddressDao;
    }

    @Override
    protected Class<UserAddress> getModelClass() {
        return UserAddress.class;
    }

    @Override
    public void updateByUserId(UserAddress userAddress) {
        userAddressDao.updateByUserId(userAddress);
    }
}
