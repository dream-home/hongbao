package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.UserBankcardDao;
import com.yanbao.mapper.UserBankcardMapper;
import com.mall.model.UserBankcard;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class UserBankcardDaoImpl implements UserBankcardDao {

	@Autowired
	private UserBankcardMapper userBankcardMapper;

	@Override
	public List<UserBankcard> getList(String userId) throws Exception {
		return userBankcardMapper.getList(userId);
	}

	@Override
	public Integer add(UserBankcard model) throws Exception {
		return userBankcardMapper.add(model);
	}

	@Override
	public Integer update(String id, UserBankcard model) throws Exception {
		return userBankcardMapper.update(id, model);
	}

}
