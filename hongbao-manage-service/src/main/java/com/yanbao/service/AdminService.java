package com.yanbao.service;

import com.yanbao.core.service.CommonService;
import com.mall.model.Admin;

/**
 * Created by summer on 2016-12-05:16:34;
 */
public interface AdminService extends CommonService<Admin> {

    public Admin getByNickName(String nickeName);
}
