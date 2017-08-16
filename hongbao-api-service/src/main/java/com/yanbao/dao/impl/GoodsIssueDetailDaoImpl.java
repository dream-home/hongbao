package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.Page;
import com.yanbao.dao.GoodsIssueDetailDao;
import com.yanbao.mapper.GoodsIssueDetailMapper;
import com.mall.model.GoodsIssueDetail;
import com.yanbao.redis.Strings;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class GoodsIssueDetailDaoImpl implements GoodsIssueDetailDao {

	@Autowired
	private GoodsIssueDetailMapper goodsIssueDetailMapper;

	@Override
	public Long players() throws Exception {
		Long count = 0L;
		String temp = Strings.get(RedisKey.HOME_PLAYERS_COUNT.getKey());
		if (null != temp) {
			count = Long.parseLong(temp);
		} else {
			count = goodsIssueDetailMapper.players();
			Strings.setEx(RedisKey.HOME_PLAYERS_COUNT.getKey(), RedisKey.HOME_PLAYERS_COUNT.getSeconds(), count.toString());
		}
		return count;
	}

	@Override
	public GoodsIssueDetail getByOrderNo(String orderNo, String userId) throws Exception {
		return goodsIssueDetailMapper.getByOrderNo(orderNo, userId);
	}

	@Override
	public Integer count(String userId) throws Exception {
		Integer count = null;
		String temp = Strings.get(RedisKey.USER_DRAW_COUNT.getKey() + userId);
		if (null != temp) {
			count = Integer.parseInt(temp);
		} else {
			count = goodsIssueDetailMapper.count(userId);
			Strings.setEx(RedisKey.USER_DRAW_COUNT.getKey() + userId, RedisKey.USER_DRAW_COUNT.getSeconds(), count.toString());
		}
		return count;
	}

	@Override
	public List<GoodsIssueDetail> getList(String userId, Page page) throws Exception {
		return goodsIssueDetailMapper.getList(userId, page);
	}

	@Override
	public Integer add(GoodsIssueDetail model) throws Exception {
		return goodsIssueDetailMapper.add(model);
	}

	@Override
	public Integer update(String id, GoodsIssueDetail model) throws Exception {
		return goodsIssueDetailMapper.update(id, model);
	}

	@Override
	public List<GoodsIssueDetail> getDrawList(String issueId, Integer status) throws Exception {
		return goodsIssueDetailMapper.getDrawList(issueId, status);
	}

	@Override
	public List<GoodsIssueDetail> getRollbackDrawList() throws Exception {
		return goodsIssueDetailMapper.getRollbackDrawList();
	}

	@Override
	public Double sumUserDrawScore(String userId) throws Exception {
		return goodsIssueDetailMapper.sumUserDrawScore(userId);
	}

	@Override
	public Integer countSumPersonByIssueId(String issueId, Integer status) throws Exception {
		return goodsIssueDetailMapper.countSumPersonByIssueId(issueId, status);
	}

}
