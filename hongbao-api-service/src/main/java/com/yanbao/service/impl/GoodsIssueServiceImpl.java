package com.yanbao.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.constant.IssueType;
import com.yanbao.dao.GoodsIssueDao;
import com.mall.model.GoodsIssue;
import com.yanbao.service.GoodsIssueService;
import com.yanbao.util.UUIDUtil;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class GoodsIssueServiceImpl implements GoodsIssueService {

	@Autowired
	private GoodsIssueDao goodsIssueDao;

	@Override
	public GoodsIssue getById(String id) throws Exception {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return goodsIssueDao.getById(id);
	}

	@Override
	public Integer add(GoodsIssue model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		return goodsIssueDao.add(model);
	}

	@Override
	public Integer update(String id, GoodsIssue model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return goodsIssueDao.update(id, model);
	}

	@Override
	public Integer updateCurNum(String id) throws Exception {
		if (StringUtils.isBlank(id)) {
			return 0;
		}
		return goodsIssueDao.updateCurNum(id);
	}
	
	private void setDefaultValue(GoodsIssue model) {
		model.setId(UUIDUtil.getUUID());
		model.setStatus(IssueType.PENDING.getCode());
		model.setCreateTime(new Date());
	}

	@Override
	public List<GoodsIssue> getTimeoutList() throws Exception {
		// 超过90天未开奖的竞拍
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -90);
		date = calendar.getTime();
		return goodsIssueDao.getTimeoutList(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
	}

}
