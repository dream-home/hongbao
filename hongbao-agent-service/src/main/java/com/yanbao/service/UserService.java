package com.yanbao.service;

import java.util.List;

import com.mall.model.User;
import com.yanbao.core.service.CommonService;
import org.apache.ibatis.annotations.Param;

/**
 * 会员相关接口
 * Created by Administrator on 2017/6/20.
 */
public interface UserService extends CommonService<User> {

    /**
     * 根据uid查询会员信息
     * @param uid
     * @return
     * @throws Exception
     */
    User readByUid(String uid) throws Exception;
    
    List<User> getByStoreIds(List<String> storeIds) throws Exception;

    public Integer updateScore(String id, Double score) throws Exception;
}
