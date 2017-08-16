package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.UserBankcard;

/**
 * Created by summer on 2016-12-12:16:47;
 */
public interface UserBankcardDao extends CommonDao<UserBankcard> {

    void updateByUserId(@Param("model") UserBankcard userBankcard);
}
