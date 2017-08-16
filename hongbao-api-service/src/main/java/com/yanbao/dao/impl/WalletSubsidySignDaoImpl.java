package com.yanbao.dao.impl;

import com.yanbao.dao.WalletSubsidySignDao;
import com.yanbao.mapper.WalletSubsidySignMapper;
import com.mall.model.WalletSubsidySign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zcj
 * @date 2017年03月03日
 */
@Repository
public class WalletSubsidySignDaoImpl implements WalletSubsidySignDao {
	@Autowired
	private WalletSubsidySignMapper signMapper;
	@Override
	public Integer add(WalletSubsidySign model) throws Exception {
		return signMapper.add(model);
	}

	@Override
	public Integer update(WalletSubsidySign model, String id) {
		return signMapper.update(model,id);
	}

	@Override
	public WalletSubsidySign getByUserId(String userid,Integer status) {
		return signMapper.getByUserId(userid,status);
	}
}
