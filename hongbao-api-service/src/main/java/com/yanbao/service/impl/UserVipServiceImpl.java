package com.yanbao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.dao.UserVipDao;
import com.yanbao.service.UserVipService;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class UserVipServiceImpl implements UserVipService {

	@Autowired
	private UserVipDao userVipDao;

	@Override
	public List<String> getVipList(List<String> ids) throws Exception {
		if (ids == null || ids.size() <= 0) {
			return null;
		}
		return userVipDao.getVipList(ids);
	}

}
