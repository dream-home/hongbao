package com.yanbao.mapper;

import org.apache.ibatis.annotations.Param;

import com.yanbao.core.dao.CommonDao;
import com.mall.model.Admin;

/**
 * Created by summer on 2016-12-05:16:42;
 */
public interface AdminDao extends CommonDao<Admin> {

    public Admin getByNickName(@Param("nickName") String nickName);
}
