package com.yanbao.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanbao.constant.DrawType;
import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.Page;
import com.yanbao.core.page.PageResult;
import com.yanbao.dao.GoodsIssueDetailDao;
import com.mall.model.GoodsIssueDetail;
import com.yanbao.redis.Hash;
import com.yanbao.service.GoodsIssueDetailService;
import com.yanbao.util.OrderNoUtil;
import com.yanbao.util.UUIDUtil;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class GoodsIssueDetailServiceImpl implements GoodsIssueDetailService {

	private static final Logger logger = LoggerFactory.getLogger(GoodsIssueDetailServiceImpl.class);

	@Autowired
	private GoodsIssueDetailDao goodsIssueDetailDao;

	@Override
	public Long players() throws Exception {
		return goodsIssueDetailDao.players();
	}

	@Override
	public GoodsIssueDetail getByOrderNo(String orderNo, String userId) throws Exception {
		if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(userId)) {
			return null;
		}
		return goodsIssueDetailDao.getByOrderNo(orderNo, userId);
	}

	@Override
	public PageResult<GoodsIssueDetail> getPage(String userId, Page page) throws Exception {
		if (userId == null) {
			return null;
		}
		PageResult<GoodsIssueDetail> pageResult = new PageResult<GoodsIssueDetail>();
		BeanUtils.copyProperties(pageResult, page);

		Integer count = goodsIssueDetailDao.count(userId);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<GoodsIssueDetail> list = goodsIssueDetailDao.getList(userId, page);
			pageResult.setRows(list);
		}
		return pageResult;
	}

	@Override
	public Integer add(GoodsIssueDetail model) throws Exception {
		if (model == null) {
			return null;
		}
		setDefaultValue(model);
		return goodsIssueDetailDao.add(model);
	}

	@Override
	public Integer update(String id, GoodsIssueDetail model) throws Exception {
		if (StringUtils.isBlank(id) || model == null) {
			return 0;
		}
		model.setUpdateTime(new Date());
		return goodsIssueDetailDao.update(id, model);
	}

	@Override
	public List<GoodsIssueDetail> getDrawList(String issueId, Integer status) throws Exception {
		return goodsIssueDetailDao.getDrawList(issueId, status);
	}

	@Override
	public List<GoodsIssueDetail> getRollbackDrawList() throws Exception {
		return goodsIssueDetailDao.getRollbackDrawList();
	}

	private void setDefaultValue(GoodsIssueDetail model) {
		model.setId(UUIDUtil.getUUID());
		model.setOrderNo(OrderNoUtil.get());
		model.setStatus(DrawType.PENDING.getCode());
		model.setCreateTime(new Date());
	}

	@Override
	public boolean checkDraw(String issueId, String userId) throws Exception {
		Long start = System.currentTimeMillis();
		String watchlist = RedisKey.WATCH_LIST.getKey() + issueId;
		Map<String, String> map = Hash.hgetAll(watchlist);
		System.out.println("**********getall**** "+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
		if (map.containsValue(userId)) {
			return true;
		}
		/*for (Map.Entry<String, String> entry : all.entrySet()) {
			if (userId.equals(entry.getValue())) {
				System.out.println("**********for**** "+(System.currentTimeMillis()-start));
				return true;
			}
		}*/
		return false;
	}

	
	@Override
	public boolean checkDrawV3(Map<String, String> map,String userId) throws Exception {
		if (map.containsValue(userId)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Double sumUserDrawScore(String userId) throws Exception {
		if (StringUtils.isBlank(userId)) {
			return 0d;
		}
		Double sumUserDrawScore = goodsIssueDetailDao.sumUserDrawScore(userId);
		if (sumUserDrawScore == null) {
			sumUserDrawScore = 0d;
		}
		return sumUserDrawScore;
	}

	@Override
	public Integer countSumPersonByIssueId(String issueId, Integer status) throws Exception {
		if (StringUtils.isBlank( issueId) ) {
			return 0;
		}
		if (status==null || status==0) {
			status=0;
		}
		return goodsIssueDetailDao.countSumPersonByIssueId(issueId, status);
	}

}
