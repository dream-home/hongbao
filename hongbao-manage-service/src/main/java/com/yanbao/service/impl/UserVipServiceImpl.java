package com.yanbao.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.constant.StatusType;
import com.yanbao.core.dao.CommonDao;
import com.yanbao.core.service.impl.CommonServiceImpl;
import com.yanbao.mapper.UserVipDao;
import com.mall.model.UserVip;
import com.yanbao.service.UserVipService;
import com.yanbao.util.UUIDUtil;

/**
 * Created by summer on 2016-12-08:16:49;
 */
@Service
public class UserVipServiceImpl extends CommonServiceImpl<UserVip> implements UserVipService {

	@Autowired
	UserVipDao userVipDao;

	@Override
	protected CommonDao<UserVip> getDao() {
		return userVipDao;
	}

	@Override
	protected Class<UserVip> getModelClass() {
		return UserVip.class;
	}

	@Override
	public Integer add(UserVip model) {
		if (model == null) {
			return 0;
		}
		setDefaultValue(model);
		return userVipDao.add(model);
	}

	private void setDefaultValue(UserVip model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
	}

	@Override
	public Integer del(String id) {
		if (StringUtils.isBlank(id)) {
			return 0;
		}
		return userVipDao.del(id);
	}

}
