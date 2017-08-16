package com.yanbao.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.Page;
import com.yanbao.dao.WalletRechargeDao;
import com.yanbao.mapper.WalletRechargeMapper;
import com.mall.model.WalletRecharge;
import com.yanbao.redis.Strings;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class WalletRechargeDaoImpl implements WalletRechargeDao {

	@Autowired
	private WalletRechargeMapper walletRechargeMapper;

	@Override
	public WalletRecharge getByOrderNo(String orderNo, String userId) throws Exception {
		return walletRechargeMapper.getByOrderNo(orderNo, userId);
	}

	@Override
	public WalletRecharge getUserOrderByOrderNo(String orderNo) throws Exception {
		return walletRechargeMapper.getUserOrderByOrderNo(orderNo);
	}

	@Override
	public Integer count(String userId) throws Exception {
		Integer count = null;
		String temp = Strings.get(RedisKey.USER_RECHARGE_COUNT.getKey() + userId);
		if (null != temp) {
			count = Integer.parseInt(temp);
		} else {
			count = walletRechargeMapper.count(userId);
			Strings.setEx(RedisKey.USER_RECHARGE_COUNT.getKey() + userId, RedisKey.USER_RECHARGE_COUNT.getSeconds(), count.toString());
		}
		return count;
	}

	@Override
	public List<WalletRecharge> getList(String userId, Page page) throws Exception {
		return walletRechargeMapper.getList(userId, page);
	}

	@Override
	public Integer add(WalletRecharge model) throws Exception {
		return walletRechargeMapper.add(model);
	}

	@Override
	public Integer update(String id, WalletRecharge model) throws Exception {
		return walletRechargeMapper.update(id, model);
	}

	@Override
	public List<WalletRecharge> getByscore(WalletRecharge wr,
			List<Integer> sources, Page page) throws Exception {
		return walletRechargeMapper.getByscore(wr, sources,page);
	}

	@Override
	public Integer countByscore(WalletRecharge wr, List<Integer> sources)
			throws Exception {
		return walletRechargeMapper.countByscore(wr, sources);
	}

	@Override
	public Double statistics(WalletRecharge wr, List<Integer> sources,
			String nowDate) throws Exception {
		return walletRechargeMapper.statistics(wr, sources, nowDate);
	}

	@Override
	public List<WalletRecharge> getListByTime(Date starttime, Date endtime,
			Integer status, Integer source) {
		return walletRechargeMapper.getListByTime(starttime, endtime, status, source);
	}

}
