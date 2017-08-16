package com.yanbao.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yanbao.constant.RedisKey;
import com.yanbao.core.page.Page;
import com.yanbao.dao.WalletDonateDao;
import com.yanbao.mapper.WalletDonateMapper;
import com.mall.model.WalletDonate;
import com.yanbao.redis.Strings;

/**
 * 
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Repository
public class WalletDonateDaoImpl implements WalletDonateDao {

	@Autowired
	private WalletDonateMapper walletDonateMapper;

	@Override
	public WalletDonate getByOrderNo(String orderNo, String userId) throws Exception {
		return walletDonateMapper.getByOrderNo(orderNo, userId);
	}

	@Override
	public Integer count(String userId) throws Exception {
		Integer count = null;
		String temp = Strings.get(RedisKey.USER_DONATE_COUNT.getKey() + userId);
		if (null != temp) {
			count = Integer.parseInt(temp);
		} else {
			count = walletDonateMapper.count(userId);
			Strings.setEx(RedisKey.USER_DONATE_COUNT.getKey() + userId, RedisKey.USER_DONATE_COUNT.getSeconds(), count.toString());
		}
		return count;
	}

	@Override
	public List<WalletDonate> getList(String userId, Page page) throws Exception {
		return walletDonateMapper.getList(userId, page);
	}

	@Override
	public Integer add(WalletDonate model) throws Exception {
		return walletDonateMapper.add(model);
	}

}
