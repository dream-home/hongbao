package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.UserVip;

/**
 * Created by summer on 2016-12-08:16:48;
 */
public interface UserVipDao extends CommonDao<UserVip> {

	Integer add(@Param("model") UserVip model);

	Integer del(@Param("id") String id);
}
