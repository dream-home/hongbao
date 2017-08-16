package com.yanbao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.constant.StatusType;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.StoreCollectDao;
import com.mall.model.StoreCollect;
import com.yanbao.service.StoreCollectService;
import com.yanbao.util.UUIDUtil;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class StoreCollectServiceImpl implements StoreCollectService {

	@Autowired
	private StoreCollectDao storeCollectDao;

	private void setDefaultValue(StoreCollect model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
		model.setUpdateTime(model.getCreateTime());
	}

	@Override
	public Boolean collect(String userId, String storeId) throws Exception {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(storeId)) {
			return false;
		}
		StoreCollect model = get(userId, storeId);
		if (model == null) {
			model = new StoreCollect();
			model.setStoreId(storeId);
			model.setUserId(userId);
			setDefaultValue(model);
			storeCollectDao.add(model);
			return true;
		} else {
			if (StatusType.FALSE.getCode() == model.getStatus()) {
				model.setStatus(StatusType.TRUE.getCode());
				model.setUpdateTime(new Date());
				storeCollectDao.update(model.getId(), model);
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean cancelCollect(String userId, String storeId) throws Exception {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(storeId)) {
			return false;
		}
		StoreCollect model = get(userId, storeId);
		if (model != null && StatusType.TRUE.getCode() == model.getStatus()) {
			model.setStatus(StatusType.FALSE.getCode());
			model.setUpdateTime(new Date());
			storeCollectDao.update(model.getId(), model);
			return true;
		}
		return false;
	}

	@Override
	public StoreCollect get(String userId, String storeId) throws Exception {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(storeId)) {
			return null;
		}
		return storeCollectDao.get(userId, storeId);
	}

	@Override
	public Boolean isCollected(String userId, String storeId) throws Exception {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(storeId)) {
			return false;
		}
		StoreCollect model = get(userId, storeId);
		if (model != null && model.getStatus() == StatusType.TRUE.getCode()) {
			return true;
		}
		return false;
	}

	@Override
	public PageResult<String> getPage(String userId, Page page) throws Exception {
		if (userId == null) {
			return null;
		}
		PageResult<String> pageResult = new PageResult<String>();
		BeanUtils.copyProperties(pageResult, page);

		Integer count = storeCollectDao.count(userId);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<String> list = storeCollectDao.getList(userId, page);
			pageResult.setRows(list);
		}
		return pageResult;
	}

}
