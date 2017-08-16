package com.yanbao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.constant.StatusType;
import com.yanbao.dao.UserBankcardDao;
import com.mall.model.UserBankcard;
import com.yanbao.service.UserBankcardService;
import com.yanbao.util.UUIDUtil;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class UserBankcardServiceImpl implements UserBankcardService {

	@Autowired
	private UserBankcardDao userBankcardDao;

	@Override
	public List<UserBankcard> getList(String userId) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		return userBankcardDao.getList(userId);
	}

	@Override
	public Integer add(UserBankcard model) throws Exception {
		if (model == null || StringUtils.isBlank(model.getUserId())) {
			return 0;
		}
		setDefaultValue(model);
		return userBankcardDao.add(model);
	}

	@Override
	public Integer update(String id, UserBankcard model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return userBankcardDao.update(id, model);
	}

	private void setDefaultValue(UserBankcard model) {
		model.setId(UUIDUtil.getUUID());
		model.setIsDefault(StatusType.TRUE.getCode());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
	}

}
