package com.yanbao.service;

import com.yanbao.core.service.CommonService;
import com.mall.model.UserBankcard;

/**
 * Created by summer on 2016-12-12:16:48;
 */
public interface UserBankcardService extends CommonService<UserBankcard> {

    void updateByUserId(UserBankcard userBankcard);
}
