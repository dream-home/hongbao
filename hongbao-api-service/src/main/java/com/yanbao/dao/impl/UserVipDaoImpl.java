package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.dao.UserVipDao;
import com.yanbao.mapper.UserVipMapper;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class UserVipDaoImpl implements UserVipDao {

	@Autowired
	private UserVipMapper userVipMapper;

	@Override
	public List<String> getVipList(List<String> ids) throws Exception {
		return userVipMapper.getVipList(ids);
	}

}
