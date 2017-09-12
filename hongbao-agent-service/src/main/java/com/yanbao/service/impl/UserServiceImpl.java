package com.yanbao.service.impl;

import java.util.List;

import com.mall.model.User;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.UserMapper;
import com.yanbao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2017/6/20.
 */
@Service
public class UserServiceImpl extends CommonServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected CommonDao<User> getDao() {
        return userMapper;
    }

    @Override
    protected Class<User> getModelClass() {
        return User.class;
    }

    @Override
    public User readByUid(String uid) throws Exception {
        if(StringUtils.isEmpty(uid)){
            return null;
        }
        return userMapper.readByUid(uid);
    }

	@Override
	public List<User> getByStoreIds(List<String> storeIds) throws Exception {
		return userMapper.getByStoreIds(storeIds);
	}

    @Override
    public Integer updateScore(String id, Double score) throws Exception {
        if (StringUtils.isEmpty(id) || score == null || score == 0) {
            return 0;
        }
        return userMapper.updateScore(id, score);
    }
}
