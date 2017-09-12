package com.yanbao.mapper;

import java.util.List;

import com.mall.model.User;
import com.yanbao.core.dao.CommonDao;
import org.apache.ibatis.annotations.Param;

/**
 * 会员相关接口
 * Created by Administrator on 2017/6/20.
 */
public interface UserMapper extends CommonDao<User> {

    /**
     * 根据uid查询会员信息
     * @param uid
     * @return
     * @throws Exception
     */
    User readByUid(@Param("uid") String uid) throws Exception;
    
    List<User> getByStoreIds(@Param("storeIds")List<String> storeIds) throws Exception;

    Integer updateScore(@Param("id") String id, @Param("score") Double score);
}
