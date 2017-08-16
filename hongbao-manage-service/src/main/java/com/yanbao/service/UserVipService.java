package com.yanbao.service;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.service.CommonService;
import com.mall.model.UserVip;

/**
 * Created by summer on 2016-12-08:16:48;
 */
public interface UserVipService extends CommonService<UserVip> {

	Integer add(@Param("model") UserVip model);

	Integer del(@Param("id") String id);
}
