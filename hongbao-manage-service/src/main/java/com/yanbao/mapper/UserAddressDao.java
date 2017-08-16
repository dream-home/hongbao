package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.UserAddress;

/**
 * Created by summer on 2016-12-12:17:01;
 */
public interface UserAddressDao extends CommonDao<UserAddress> {

    void updateByUserId(@Param("model") UserAddress userAddress);
}
