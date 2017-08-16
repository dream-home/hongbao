package com.yanbao.service.impl;

import com.yanbao.constant.StatusType;
import com.yanbao.dao.FeedBackDao;
import com.mall.model.FeedBack;
import com.yanbao.service.FeedBackService;
import com.yanbao.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 
 * @author zzwei
 * @date 2017年06月27日
 */
@Service
public class FeedBackServiceImpl implements FeedBackService {

	@Autowired
	private FeedBackDao feedBackDao;

	@Override
	public FeedBack getById(String id) throws Exception {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return feedBackDao.getById(id);
	}


	@Override
	public Integer add(FeedBack model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		return feedBackDao.add(model);
	}


	private void setDefaultValue(FeedBack model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.FALSE.getCode());
		model.setCreateTime(new Date());
	}

}
