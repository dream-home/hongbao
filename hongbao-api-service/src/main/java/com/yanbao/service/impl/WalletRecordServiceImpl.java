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
import com.yanbao.dao.WalletRecordDao;
import com.mall.model.WalletRecord;
import com.yanbao.service.WalletRecordService;
import com.yanbao.util.UUIDUtil;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class WalletRecordServiceImpl implements WalletRecordService {

	@Autowired
	private WalletRecordDao walletRecordDao;

	@Override
	public PageResult<WalletRecord> getPage(String userId, String[] recordTypes, Page page) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		PageResult<WalletRecord> pageResult = new PageResult<WalletRecord>();
		BeanUtils.copyProperties(pageResult, page);

		Integer count = walletRecordDao.count(userId, recordTypes);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<WalletRecord> list = walletRecordDao.getList(userId, recordTypes, page);
			pageResult.setRows(list);
		}
		return pageResult;
	}

	@Override
	public Integer add(WalletRecord model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		return walletRecordDao.add(model);
	}

	@Override
	public Integer update(String id, WalletRecord model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return walletRecordDao.update(id, model);
	}

	private void setDefaultValue(WalletRecord model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(StatusType.TRUE.getCode());
		model.setCreateTime(new Date());
	}

	@Override
	public Double sumScore(String userId, Integer recordType) throws Exception {
		if (StringUtils.isBlank(userId) || recordType == null) {
			return 0d;
		}
		Double sumScore = walletRecordDao.sumScore(userId, recordType);
		if (sumScore == null) {
			sumScore = 0d;
		}
		return sumScore;
	}

	@Override
	public List<WalletRecord> getLatestList(String userId) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		return walletRecordDao.getLatestList(userId);
	}
}
