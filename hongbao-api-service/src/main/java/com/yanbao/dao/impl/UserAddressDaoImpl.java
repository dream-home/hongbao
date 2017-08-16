package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.UserAddressDao;
import com.yanbao.mapper.UserAddressMapper;
import com.mall.model.UserAddress;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class UserAddressDaoImpl implements UserAddressDao {

	@Autowired
	private UserAddressMapper userAddressMapper;

	@Override
	public List<UserAddress> getList(String userId) throws Exception {
		return userAddressMapper.getList(userId);
	}

	@Override
	public Integer add(UserAddress model) throws Exception {
		return userAddressMapper.add(model);
	}

	@Override
	public Integer update(String id, UserAddress model) throws Exception {
		return userAddressMapper.update(id, model);
	}

	@Override
	public Integer updateDefaultAddr(String userId) throws Exception {
		return userAddressMapper.updateDefaultAddr(userId);
	}

}
